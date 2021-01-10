package io.github.ititus.skat.game;

import io.github.ititus.skat.network.buffer.PacketBufferSerializer;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

import java.util.Comparator;

public final class Player implements PacketBufferSerializer, Comparable<Player> {

    public static final Comparator<Player> ACTIVE_COMPARATOR = (p1, p2) -> {
        if (p1.gupid == p2.gupid) {
            return 0;
        } else if (p1.activePlayerIndex >= 0 && p2.activePlayerIndex >= 0) {
            return Byte.compare(p1.activePlayerIndex, p2.activePlayerIndex);
        } else if (p1.activePlayerIndex < 0 && p2.activePlayerIndex >= 0) {
            return -1;
        } else if (p1.activePlayerIndex >= 0) {
            return 1;
        }

        return Byte.compare(p1.gupid, p2.gupid);
    };

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

    public Player withActivePlayerIndex(byte ap) {
        return new Player(gupid, ap, name);
    }

    public Player withName(String name) {
        return new Player(gupid, activePlayerIndex, name);
    }

    @Override
    public String toString() {
        return "Player{" +
                "gupid=" + gupid +
                ", activePlayerIndex=" + activePlayerIndex +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Player player = (Player) o;
        return gupid == player.gupid;
    }

    @Override
    public int hashCode() {
        return Byte.hashCode(gupid);
    }

    @Override
    public int compareTo(Player o) {
        return Byte.compare(gupid, o.gupid);
    }
}
