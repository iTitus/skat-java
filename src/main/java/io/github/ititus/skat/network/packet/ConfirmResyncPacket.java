package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class ConfirmResyncPacket implements ServerboundPacket {

    @Override
    public void write(WritablePacketBuffer buf) {
    }

    @Override
    public ServerboundPacketType getServerboundType() {
        return ServerboundPacketType.CONFIRM_RESYNC;
    }
}
