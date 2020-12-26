package io.github.ititus.skat.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Arrays;

public class TestClientHandler extends ChannelInboundHandlerAdapter {

    private final ByteBuf firstMessage;

    public TestClientHandler() {
        firstMessage = Unpooled.buffer();

        firstMessage.writeByte(1);
        firstMessage.writeByte(0);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("TestClientHandler.channelActive");

        ByteBuf sizeBuf = ctx.alloc().buffer(4);
        sizeBuf.writeInt(firstMessage.readableBytes());

        CompositeByteBuf composite = ctx.alloc().compositeBuffer();
        composite.addComponents(true, sizeBuf, firstMessage);

        ctx.writeAndFlush(composite);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("TestClientHandler.channelRead: " + msg);

        if (!(msg instanceof ByteBuf)) {
            throw new RuntimeException("expected byte buf");
        }

        ByteBuf b = (ByteBuf) msg;
        int size = Math.toIntExact(b.readUnsignedInt());

        byte[] bytes = new byte[size];
        b.readBytes(bytes);
        System.out.println("  size=" + size + ": " + Arrays.toString(bytes));

        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("TestClientHandler.channelReadComplete");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("TestClientHandler.exceptionCaught");
        cause.printStackTrace();
        ctx.close();
    }
}
