package io.github.ititus.skat.network.buffer;

@FunctionalInterface
public interface PacketBufferDeserializer<T> {

    T read(ReadablePacketBuffer buf);

}
