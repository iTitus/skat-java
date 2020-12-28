package io.github.ititus.skat.network.buffer;

import io.github.ititus.skat.network.NetworkEnum;

import java.math.BigInteger;

public interface WritablePacketBuffer extends PacketBuffer {

    void writeBoolean(boolean b);

    void writeString(String s);

    void writeByte(byte n);

    void writeUnsignedByte(short n);

    void writeShort(short n);

    void writeUnsignedShort(int n);

    void writeInt(int n);

    void writeUnsignedInt(long n);

    void writeLong(long n);

    void writeUnsignedLong(BigInteger n);

    <T extends NetworkEnum<T>> void writeEnum(T value);

}
