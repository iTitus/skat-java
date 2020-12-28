package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class ConfirmJoinPacket extends Packet {

    public ConfirmJoinPacket(ReadablePacketBuffer buf) {
        super(PacketType.CONFIRM_JOIN);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }

    @Override
    public void handleClient() {
    }
}
