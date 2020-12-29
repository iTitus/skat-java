package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.network.NetworkManager;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class ResumePacket implements ServerboundPacket {

    private final int networkProtocolVersion;
    private final String name;

    public ResumePacket(String name) {
        this.networkProtocolVersion = NetworkManager.VERSION;
        this.name = name;
    }

    @Override
    public void write(WritablePacketBuffer buf) {
        buf.writeUnsignedShort(networkProtocolVersion);
        buf.writeString(name);
    }

    @Override
    public ServerboundPacketType getServerboundType() {
        return ServerboundPacketType.RESUME;
    }
}
