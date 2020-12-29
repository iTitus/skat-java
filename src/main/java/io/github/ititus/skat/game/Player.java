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

    public Player(byte gupid, String name) {
        this(gupid, (byte) -1, name);
    }

    public Player(byte gupid, byte activePlayerIndex, String name) {
        this.gupid = gupid;
        this.activePlayerIndex = activePlayerIndex;
        this.name = name;
    }

    @Override
    public void write(WritablePacketBuffer buf) {
        buf.writeByte(gupid);
        buf.writeByte(activePlayerIndex);
        buf.writeString(name);
    }

    public byte getGupid() {
        return gupid;
    }

    public byte getActivePlayerIndex() {
        return activePlayerIndex;
    }

    public boolean isPlaying() {
        return activePlayerIndex >= 0;
    }

    public String getName() {
        return name;
    }
}
