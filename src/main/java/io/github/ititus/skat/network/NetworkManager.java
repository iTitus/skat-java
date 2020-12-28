package io.github.ititus.skat.network;

import io.github.ititus.skat.network.packet.Packet;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.function.Consumer;

public class NetworkManager extends SimpleChannelInboundHandler<Packet> {

    public static final int VERSION = 4;

    private final InetSocketAddress socketAddress;
    private final Runnable connectionEstablishedListener;
    private final EventLoopGroup eventLoopGroup;
    private final ChannelFuture channelFuture;
    private final Runnable disconnectListener;

    public NetworkManager(String host, int port, Runnable connectionEstablishedListener,
                          Consumer<Throwable> connectionFailedListener, Runnable disconnectListener) {
        this.socketAddress = InetSocketAddress.createUnresolved(host, port);
        this.connectionEstablishedListener = connectionEstablishedListener;
        this.disconnectListener = disconnectListener;

        this.eventLoopGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("Netty Event Loop Thread #", true));
        this.channelFuture = new Bootstrap()
                .group(this.eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .remoteAddress(this.socketAddress)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast("length_decoder",
                                        new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                                .addLast("packet_decoder", new PacketDecoder())
                                .addLast("length_encoder", new LengthFieldPrepender(4))
                                .addLast("packet_encoder", new PacketEncoder())
                                .addLast("packet_handler", NetworkManager.this);
                    }
                })
                .connect();

        this.channelFuture.addListener(f -> {
            if (!f.isSuccess()) {
                connectionFailedListener.accept(f.cause());
            }
        });
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("NetworkManager.channelActive");

        connectionEstablishedListener.run();
        ctx.channel().closeFuture().addListener(f -> disconnectListener.run());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("NetworkManager.channelInactive");

        stop();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet p) {
        System.out.println("NetworkManager.channelRead0: p=" + p);

        // TODO: handle this on a different thread
        p.handleClient();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("NetworkManager.exceptionCaught");

        cause.printStackTrace();
        stop();
    }

    public void stop() {
        stopAsync().ifPresent(ChannelFuture::awaitUninterruptibly);
    }

    public Optional<ChannelFuture> stopAsync() {
        if (!isChannelOpen()) {
            return Optional.empty();
        }

        channelFuture.channel().close();
        eventLoopGroup.shutdownGracefully();
        return Optional.of(channelFuture.channel().closeFuture());
    }

    public boolean isChannelOpen() {
        return channelFuture.channel().isOpen();
    }

    public void sendPacket(Packet p) {
        if (!isChannelOpen()) {
            throw new IllegalStateException("channel is closed");
        }

        Runnable sender = () -> {
            ChannelFuture f = channelFuture.channel().writeAndFlush(p);
            f.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        };

        if (channelFuture.channel().eventLoop().inEventLoop()) {
            sender.run();
        } else {
            channelFuture.channel().eventLoop().execute(sender);
        }
    }
}
