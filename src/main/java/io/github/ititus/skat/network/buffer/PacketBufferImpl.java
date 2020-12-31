package io.github.ititus.skat.network.buffer;

import io.github.ititus.skat.network.NetworkEnum;
import io.github.ititus.skat.util.MathUtil;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Function;

import static io.github.ititus.skat.util.MathUtil.*;

public class PacketBufferImpl implements ReadablePacketBuffer, WritablePacketBuffer {

    private static final boolean DEFAULT_DEBUG_TYPES = true;

    private final ByteBuf buf;
    private final int startReaderIndex;
    private final boolean debugTypes;

    public PacketBufferImpl(ByteBuf buf) {
        this(buf, DEFAULT_DEBUG_TYPES);
    }

    PacketBufferImpl(ByteBuf buf, boolean debugTypes) {
        this.buf = buf;
        this.startReaderIndex = buf.readerIndex();
        this.debugTypes = debugTypes;
    }

    private void checkType(Type expected) {
        Type actual = Type.fromId(buf.readByte());
        if (actual != expected) {
            throw new RuntimeException("expected type " + expected + " but got " + actual);
        }
    }

    private void writeType(Type type) {
        buf.writeByte(type.getId());
    }

    @Override
    public int readableBytes() {
        return buf.readableBytes();
    }

    @Override
    public boolean readBoolean() {
        if (debugTypes) {
            checkType(Type.BOOL);
        }

        return buf.readBoolean();
    }

    @Override
    public void writeBoolean(boolean b) {
        if (debugTypes) {
            writeType(Type.BOOL);
        }

        buf.writeBoolean(b);
    }

    @Override
    public String readString() {
        if (debugTypes) {
            checkType(Type.STR);
        }

        ByteList byteList = new ByteArrayList();
        while (true) {
            byte b = buf.readByte();
            if (b == 0) {
                break;
            }

            byteList.add(b);
        }

        return new String(byteList.toByteArray(), StandardCharsets.UTF_8);
    }

    @Override
    public void writeString(String s) {
        if (s.indexOf('\0') >= 0) {
            throw new IllegalArgumentException("string may not contain null-characters");
        }

        if (debugTypes) {
            writeType(Type.STR);
        }

        buf.writeBytes(s.getBytes(StandardCharsets.UTF_8));
        buf.writeByte(0);
    }

    @Override
    public byte readByte() {
        if (debugTypes) {
            checkType(Type.I8);
        }

        return buf.readByte();
    }

    @Override
    public byte[] readBytes(int length) {
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = readByte();
        }

        return bytes;
    }

    @Override
    public short readUnsignedByte() {
        if (debugTypes) {
            checkType(Type.U8);
        }

        return buf.readUnsignedByte();
    }

    @Override
    public short[] readUnsignedBytes(int length) {
        short[] bytes = new short[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = readUnsignedByte();
        }

        return bytes;
    }

    @Override
    public void writeByte(byte n) {
        if (debugTypes) {
            writeType(Type.I8);
        }

        buf.writeByte(n);
    }

    @Override
    public void writeUnsignedByte(short n) {
        if (n < 0 || n > UNSIGNED_BYTE_MAX_VALUE) {
            throw new ArithmeticException("Given integer " + n + " is not an unsigned byte");
        } else if (debugTypes) {
            writeType(Type.U8);
        }

        buf.writeByte(n);
    }

    private long readVarInt(Type type) {
        if (!type.isVarInt()) {
            throw new IllegalArgumentException("given type " + type + " is not a var int type");
        }

        if (debugTypes) {
            checkType(type);
        }

        int bits = type.bitCount();
        int maxBytes = MathUtil.ceilDiv(bits, 7);
        int superfluousBits = (maxBytes * 7) - bits;
        int mask = -1 << (7 - superfluousBits);

        long zigzag = 0;
        int usedBytes = 0;
        byte b;
        do {
            b = buf.readByte();
            int data = b & 0b01111111;
            if (usedBytes == maxBytes - 1 && (data & mask) != 0) {
                throw new RuntimeException("var int too long for type " + type);
            }

            zigzag |= (long) data << (7 * usedBytes++);
        } while ((b & 0b10000000) != 0);

        long result = (zigzag >>> 1) ^ -(zigzag & 1);
        if (result > (1L << (bits - 1)) - 1 || result < -1L << (bits - 1)) {
            throw new ArithmeticException("result integer " + result + " out of bounds for type " + type);
        }

        return result;
    }

    private void writeVarInt(Type type, long n) {
        if (!type.isVarInt()) {
            throw new IllegalArgumentException("given type " + type + " is not a var int type");
        }

        int bits = type.bitCount();
        if (n > (1L << (bits - 1)) - 1 || n < -1L << (bits - 1)) {
            throw new ArithmeticException("given integer " + n + " out of bounds for type " + type);
        }

        if (debugTypes) {
            writeType(type);
        }

        long zigzag = (n >> (Long.SIZE - 1)) ^ (n << 1);
        do {
            int data = (int) zigzag & 0b01111111;
            zigzag = zigzag >>> 7;
            if (zigzag != 0) {
                data |= 0b10000000;
            }

            buf.writeByte(data);
        } while (zigzag != 0);
    }

    @Override
    public short readShort() {
        return (short) readVarInt(Type.VAR_I16);
    }

    @Override
    public int readUnsignedShort() {
        return Short.toUnsignedInt((short) readVarInt(Type.VAR_U16));
    }

    @Override
    public void writeShort(short n) {
        writeVarInt(Type.VAR_I16, n);
    }

    @Override
    public void writeUnsignedShort(int n) {
        if (n < 0 || n > UNSIGNED_SHORT_MAX_VALUE) {
            throw new ArithmeticException("Given integer " + n + " is not an unsigned short");
        }

        writeVarInt(Type.VAR_U16, (short) n);
    }

    @Override
    public int readInt() {
        return (int) readVarInt(Type.VAR_I32);
    }

    @Override
    public long readUnsignedInt() {
        return Integer.toUnsignedLong((int) readVarInt(Type.VAR_U32));
    }

    @Override
    public void writeInt(int n) {
        writeVarInt(Type.VAR_I32, n);
    }

    @Override
    public void writeUnsignedInt(long n) {
        if (n < 0 || n > UNSIGNED_INT_MAX_VALUE) {
            throw new ArithmeticException("Given integer " + n + " is not an unsigned int");
        }

        writeVarInt(Type.VAR_U32, (int) n);
    }

    @Override
    public long readLong() {
        return readVarInt(Type.VAR_I64);
    }

    @Override
    public long[] readLongs(int length) {
        long[] longs = new long[length];
        for (int i = 0; i < length; i++) {
            longs[i] = readLong();
        }

        return longs;
    }

    @Override
    public BigInteger readUnsignedLong() {
        return MathUtil.toUnsignedBigInteger(readVarInt(Type.VAR_U64));
    }

    @Override
    public void writeLong(long n) {
        writeVarInt(Type.VAR_I64, n);
    }

    @Override
    public void writeUnsignedLong(BigInteger n) {
        if (n.signum() < 0 || n.compareTo(UNSIGNED_LONG_MAX_VALUE) > 0) {
            throw new ArithmeticException("Given integer " + n + " is not an unsigned long");
        }

        writeVarInt(Type.VAR_U64, n.longValue());
    }

    @Override
    public <T extends NetworkEnum<T>> T readEnum(Byte2ObjectFunction<T> enumFactory) {
        return Objects.requireNonNull(enumFactory.get(readByte()), "enum value must not be null");
    }

    @Override
    public <T extends NetworkEnum<T>> T readNullableEnum(Byte2ObjectFunction<T> enumFactory) {
        return enumFactory.get(readByte());
    }

    @Override
    public <T extends NetworkEnum<T>> void writeEnum(T value) {
        writeByte(value.getId());
    }

    @Override
    public <T extends NetworkEnum<T>> void writeNullableEnum(T value) {
        writeByte(value != null ? value.getId() : 0);
    }

    @Override
    public String dump() {
        StringBuilder b = new StringBuilder("PacketBuffer[");
        b.append("ridx=").append(buf.readerIndex()).append(',');
        b.append("widx=").append(buf.writerIndex()).append(',');
        b.append("cap=").append(buf.capacity());
        if (buf.maxCapacity() < Integer.MAX_VALUE) {
            b.append('/').append(buf.maxCapacity());
        }
        b.append("]:");

        int currentIndex = buf.readerIndex();
        buf.readerIndex(startReaderIndex);
        if (debugTypes) {
            while (buf.isReadable()) {
                Type type = Type.fromId(buf.getByte(buf.readerIndex()));
                b.append(' ').append(type.toString(this));
            }
        } else {
            while (buf.isReadable()) {
                b.append(String.format(" %02x", buf.readUnsignedByte()));
            }
        }
        buf.readerIndex(currentIndex);

        return b.toString();
    }

    enum Type {

        I8(1, "i8", ReadablePacketBuffer::readByte),
        U8(2, "u8", ReadablePacketBuffer::readUnsignedByte),
        VAR_I16(3, "i16", ReadablePacketBuffer::readShort),
        VAR_I32(4, "i32", ReadablePacketBuffer::readInt),
        VAR_I64(5, "i64", ReadablePacketBuffer::readLong),
        VAR_U16(6, "u16", ReadablePacketBuffer::readUnsignedShort),
        VAR_U32(7, "u32", ReadablePacketBuffer::readUnsignedInt),
        VAR_U64(8, "u64", ReadablePacketBuffer::readUnsignedLong),
        BOOL(9, "b", ReadablePacketBuffer::readBoolean),
        STR(10, "s", buf -> "'" + buf.readString() + "'");

        private final byte id;
        private final String symbol;
        private final Function<ReadablePacketBuffer, ?> extractor;

        Type(int id, String symbol, Function<ReadablePacketBuffer, ?> extractor) {
            this.id = (byte) id;
            this.symbol = symbol;
            this.extractor = extractor;
        }

        static Type fromId(byte id) {
            switch (id) {
                case 1:
                    return I8;
                case 2:
                    return U8;
                case 3:
                    return VAR_I16;
                case 4:
                    return VAR_I32;
                case 5:
                    return VAR_I64;
                case 6:
                    return VAR_U16;
                case 7:
                    return VAR_U32;
                case 8:
                    return VAR_U64;
                case 9:
                    return BOOL;
                case 10:
                    return STR;
                default:
                    throw new IllegalArgumentException("unknown type id " + id);
            }
        }

        byte getId() {
            return id;
        }

        String toString(ReadablePacketBuffer buf) {
            return symbol + ":" + extractor.apply(buf);
        }

        boolean isVarInt() {
            switch (this) {
                case VAR_I16:
                case VAR_I32:
                case VAR_I64:
                case VAR_U16:
                case VAR_U32:
                case VAR_U64:
                    return true;
                default:
                    return false;
            }
        }

        int bitCount() {
            switch (this) {
                case I8:
                case U8:
                    return 8;
                case VAR_I16:
                case VAR_U16:
                    return 16;
                case VAR_I32:
                case VAR_U32:
                    return 32;
                case VAR_I64:
                case VAR_U64:
                    return 64;
                default:
                    throw new RuntimeException(this + " is not an integer type");
            }
        }

        boolean isUnsigned() {
            switch (this) {
                case I8:
                case VAR_I16:
                case VAR_I32:
                case VAR_I64:
                    return false;
                case U8:
                case VAR_U16:
                case VAR_U32:
                case VAR_U64:
                    return true;
                default:
                    throw new RuntimeException(this + " is not an integer type");
            }
        }
    }
}
