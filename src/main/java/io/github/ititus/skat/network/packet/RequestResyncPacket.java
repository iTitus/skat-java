package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class RequestResyncPacket implements ServerboundPacket {

    @Override
    public void write(WritablePacketBuffer buf) {
    }

    @Override
    public ServerboundPacketType getServerboundType() {
        return ServerboundPacketType.REQUEST_RESYNC;
    }
}
