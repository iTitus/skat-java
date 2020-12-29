package io.github.ititus.skat.network;

import io.github.ititus.skat.network.buffer.PacketBufferImpl;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.packet.ClientboundPacket;
import io.github.ititus.skat.network.packet.ClientboundPacketType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.IOException;
import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("PacketDecoder.decode: in=" + in);

        ReadablePacketBuffer buf = new PacketBufferImpl(in);
        System.out.println("  Received: " + buf.dump());

        byte id = buf.readByte();
        ClientboundPacket p = ClientboundPacketType.fromId(id).read(buf);

        if (buf.readableBytes() > 0) {
            throw new IOException("Packet " + p.getClass().getSimpleName() + " (" + id + ") was larger than expected,"
                    + " got " + buf.readableBytes() + " extra bytes");
        }

        out.add(p);
    }
}
