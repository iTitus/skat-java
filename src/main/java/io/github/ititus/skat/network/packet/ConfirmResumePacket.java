package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class ConfirmResumePacket extends Packet {

    public ConfirmResumePacket(ReadablePacketBuffer buf) {
        super(PacketType.CONFIRM_RESUME);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }

    @Override
    public void handleClient() {
    }
}
