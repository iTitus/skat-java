package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.netty.channel.ChannelHandlerContext;

public class ConfirmResumePacket implements ClientboundPacket {

    private final byte gupid;

    public ConfirmResumePacket(ReadablePacketBuffer buf) {
        gupid = buf.readByte();
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
    }

    @Override
    public ClientboundPacketType getClientboundType() {
        return ClientboundPacketType.CONFIRM_RESUME;
    }
}
