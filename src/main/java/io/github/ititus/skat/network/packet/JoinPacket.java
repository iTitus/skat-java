package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class JoinPacket extends Packet {

    public JoinPacket(ReadablePacketBuffer buf) {
        super(PacketType.JOIN);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }

    @Override
    public void handleClient() {
        throw new UnsupportedOperationException("not handled on client side");
    }
}
