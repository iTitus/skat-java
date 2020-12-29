package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.netty.channel.ChannelHandlerContext;

public class ConfirmJoinPacket implements ClientboundPacket {

    private final byte gupid;

    public ConfirmJoinPacket(ReadablePacketBuffer buf) {
        gupid = buf.readByte();
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
    }

    @Override
    public ClientboundPacketType getClientboundType() {
        return ClientboundPacketType.CONFIRM_JOIN;
    }
}
