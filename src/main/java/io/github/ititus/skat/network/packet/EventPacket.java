package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class EventPacket extends Packet {

    public EventPacket(ReadablePacketBuffer buf) {
        super(PacketType.EVENT);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }

    @Override
    public void handleClient() {
    }
}
