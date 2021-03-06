package io.github.ititus.skat.network.buffer;

import io.github.ititus.skat.network.NetworkEnum;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;

import java.math.BigInteger;

public interface ReadablePacketBuffer extends PacketBuffer {

    int readableBytes();

    boolean readBoolean();

    String readString();

    byte readByte();

    byte[] readBytes(int length);

    short readUnsignedByte();

    short[] readUnsignedBytes(int length);

    short readShort();

    int readUnsignedShort();

    int readInt();

    long readUnsignedInt();

    long readLong();

    long[] readLongs(int length);

    BigInteger readUnsignedLong();

    <T extends NetworkEnum<T>> T readEnum(Byte2ObjectFunction<T> enumFactory);

    <T extends NetworkEnum<T>> T readNullableEnum(Byte2ObjectFunction<T> enumFactory);

}
