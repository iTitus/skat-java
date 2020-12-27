package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public interface Packet {

    void read(ReadablePacketBuffer buf);

    void write(WritablePacketBuffer buf);

}
