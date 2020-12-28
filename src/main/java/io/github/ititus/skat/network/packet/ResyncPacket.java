package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class ResyncPacket extends Packet {

    public ResyncPacket(ReadablePacketBuffer buf) {
        super(PacketType.RESYNC);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }

    @Override
    public void handleClient() {
    }
}
