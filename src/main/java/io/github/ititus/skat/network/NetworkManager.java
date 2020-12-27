package io.github.ititus.skat.network;

import io.github.ititus.skat.network.packet.Packet;
import io.github.ititus.skat.network.packet.TestPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;

import java.net.InetSocketAddress;

public class NetworkManager extends SimpleChannelInboundHandler<Packet> {

    public static final AttributeKey<NetworkProtocol> PROTOCOL = AttributeKey.newInstance("protocol");

    private final InetSocketAddress socketAddress;
    private final NetworkProtocol protocol;
    private final Runnable connectionEstablishedListener;
    private final EventLoopGroup eventLoopGroup;
    private final ChannelFuture channelFuture;

    public NetworkManager(String host, int port, Runnable connectionEstablishedListener) {
        this.socketAddress = InetSocketAddress.createUnresolved(host, port);
        this.protocol = new NetworkProtocol();
        this.connectionEstablishedListener = connectionEstablishedListener;
        this.eventLoopGroup = new NioEventLoopGroup();
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
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("NetworkManager.channelActive");

        ctx.channel().attr(PROTOCOL).set(protocol);

        connectionEstablishedListener.run();

        ctx.writeAndFlush(new TestPacket());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet p) {
        System.out.println("NetworkManager.channelRead0: p=" + p);

        if (p instanceof TestPacket) {
            System.out.println("  content=" + ((TestPacket) p).getContent());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("NetworkManager.exceptionCaught");

        cause.printStackTrace();
        ctx.close();
    }

    public void stop() throws Exception {
        ChannelFuture close = channelFuture.channel().closeFuture();
        Future<?> shutdown = eventLoopGroup.shutdownGracefully();

        close.sync();
        shutdown.sync();
    }
}
