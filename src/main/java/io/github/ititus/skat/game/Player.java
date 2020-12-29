package io.github.ititus.skat.game;

import io.github.ititus.skat.network.buffer.PacketBufferSerializer;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public final class Player implements PacketBufferSerializer {

    private final byte gupid;
    private final byte activePlayerIndex;
    private final String name;

    public Player(ReadablePacketBuffer buf) {
        gupid = buf.readByte();
        activePlayerIndex = buf.readByte();
        name = buf.readString();
    }

    @Override
    public void write(WritablePacketBuffer buf) {
        buf.writeByte(gupid);
        buf.writeByte(activePlayerIndex);
        buf.writeString(name);
    }
}
