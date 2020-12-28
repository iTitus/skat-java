package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.network.buffer.PacketBufferSerializer;

public abstract class Packet implements PacketBufferSerializer {

    private final PacketType type;

    protected Packet(PacketType type) {
        this.type = type;
    }

    public PacketType getType() {
        return type;
    }

    public abstract void handleClient();

}
