package io.github.ititus.skat.network;

import io.github.ititus.data.pair.Pair;
import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.network.handler.NetHandler;
import io.github.ititus.skat.network.packet.ClientboundPacket;
import io.github.ititus.skat.network.packet.ServerboundPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.function.Consumer;

public class NetworkManager extends SimpleChannelInboundHandler<ClientboundPacket> {

    public static final int VERSION = 4;

    private static final AttributeKey<Pair<ConnectionState, NetHandler>> STATE_KEY = AttributeKey.valueOf("state");

    private final SkatClient skatClient;
    private final InetSocketAddress socketAddress;
    private final Runnable connectionEstablishedListener;
    private final EventLoopGroup eventLoopGroup;
    private final ChannelFuture channelFuture;
    private final Runnable disconnectListener;

    public NetworkManager(SkatClient skatClient, String host, int port, Runnable connectionEstablishedListener,
                          Consumer<Throwable> connectionFailedListener, Runnable disconnectListener) {
        this.skatClient = skatClient;
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

    public static NetHandler getNetHandler(Channel channel) {
        return channel.attr(STATE_KEY).get().b();
    }

    public static ConnectionState getConnectionState(Channel channel) {
        return channel.attr(STATE_KEY).get().a();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("NetworkManager.channelActive");

        setNetHandler(ctx.channel(), ConnectionState.JOIN);

        ctx.channel().closeFuture().addListener(f -> disconnectListener.run());
        connectionEstablishedListener.run();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("NetworkManager.channelInactive");

        stop();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientboundPacket p) {
        System.out.println("NetworkManager.channelRead0: p=" + p);

        // TODO: handle this on a different thread
        p.handle(ctx);
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

    public void sendPacket(ServerboundPacket p) {
        sendPacket(p, null);
    }

    public void sendPacket(ServerboundPacket p, GenericFutureListener<? extends Future<? super Void>> listener) {
        if (!isChannelOpen()) {
            throw new IllegalStateException("channel is closed");
        }

        Runnable sender = () -> {
            ChannelFuture f = channelFuture.channel().writeAndFlush(p);
            if (listener != null) {
                f.addListener(listener);
            }
            f.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        };

        if (channelFuture.channel().eventLoop().inEventLoop()) {
            sender.run();
        } else {
            channelFuture.channel().eventLoop().execute(sender);
        }
    }

    private void setNetHandler(Channel channel, ConnectionState state) {
        channel.attr(STATE_KEY).set(Pair.of(state, state.createNetHandler(skatClient, this)));
    }
}
