package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class ConfirmResyncPacket extends Packet {

    public ConfirmResyncPacket(ReadablePacketBuffer buf) {
        super(PacketType.CONFIRM_RESYNC);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }

    @Override
    public void handleClient() {
        throw new UnsupportedOperationException("not handled on client side");
    }
}
