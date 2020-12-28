package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class NotifyLeavePacket extends Packet {

    public NotifyLeavePacket(ReadablePacketBuffer buf) {
        super(PacketType.NOTIFY_LEAVE);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }

    @Override
    public void handleClient() {
    }
}
