package io.github.ititus.skat.network.buffer;

@FunctionalInterface
public interface PacketBufferSerializer {

    void write(WritablePacketBuffer buf);

}
