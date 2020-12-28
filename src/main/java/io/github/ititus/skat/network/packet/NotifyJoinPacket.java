package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class NotifyJoinPacket extends Packet {

    public NotifyJoinPacket(ReadablePacketBuffer buf) {
        super(PacketType.NOTIFY_JOIN);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }

    @Override
    public void handleClient() {
    }
}
