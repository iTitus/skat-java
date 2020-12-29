package io.github.ititus.skat.network;

import io.github.ititus.skat.network.buffer.PacketBufferImpl;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;
import io.github.ititus.skat.network.packet.ServerboundPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<ServerboundPacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ServerboundPacket p, ByteBuf out) {
        System.out.println("PacketEncoder.encode: p=" + p);

        WritablePacketBuffer buf = new PacketBufferImpl(out);

        byte id = p.getServerboundType().getId();
        buf.writeByte(id);

        p.write(buf);

        System.out.println("  Sending: " + buf.dump());
    }
}
