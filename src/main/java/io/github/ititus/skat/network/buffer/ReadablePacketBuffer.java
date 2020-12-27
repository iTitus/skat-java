package io.github.ititus.skat.network.buffer;

import java.math.BigInteger;

public interface ReadablePacketBuffer extends PacketBuffer {

    int readableBytes();

    boolean readBoolean();

    String readString();

    byte readByte();

    short readUnsignedByte();

    short readShort();

    int readUnsignedShort();

    int readInt();

    long readUnsignedInt();

    long readLong();

    BigInteger readUnsignedLong();

}
