package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.netty.channel.ChannelHandlerContext;

public class NotifyLeavePacket implements ClientboundPacket {

    private final byte gupid;

    public NotifyLeavePacket(ReadablePacketBuffer buf) {
        gupid = buf.readByte();
    }

    @Override
    public void handle(ChannelHandlerContext ctx, SkatClient skatClient) {
    }

    @Override
    public ClientboundPacketType getClientboundType() {
        return ClientboundPacketType.NOTIFY_LEAVE;
    }
}
