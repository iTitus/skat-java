package io.github.ititus.skat.network.buffer;

import io.github.ititus.math.number.JavaMath;
import io.github.ititus.skat.network.NetworkEnum;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Function;

import static io.github.ititus.precondition.BooleanPrecondition.isTrue;
import static io.github.ititus.precondition.ComparablePrecondition.inBoundsInclusive;
import static io.github.ititus.precondition.IntPrecondition.inBounds;
import static io.github.ititus.precondition.IntPrecondition.inBoundsInclusive;
import static io.github.ititus.precondition.LongPrecondition.inBoundsInclusive;
import static io.github.ititus.precondition.Precondition.equalTo;
import static io.github.ititus.precondition.Preconditions.check;
import static io.github.ititus.precondition.StringPrecondition.notContains;

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
        check(actual, equalTo(expected));
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
        check(s, notContains('\0'));

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
        check(n, inBoundsInclusive(JavaMath.UNSIGNED_BYTE_MAX_VALUE));

        if (debugTypes) {
            writeType(Type.U8);
        }

        buf.writeByte(n);
    }

    private long readVarInt(Type type) {
        check(type.isVarInt(), isTrue());

        if (debugTypes) {
            checkType(type);
        }

        int bits = 8 * type.getByteCount();
        int maxBytes = JavaMath.ceilDiv(bits, 7);
        int superfluousBits = 7 * maxBytes - bits;
        int mask = -1 << 7 - superfluousBits;

        long zigzag = 0;
        int usedBytes = 0;
        byte b;
        do
        {
            b = buf.readByte();
            int data = b & 0b01111111;
            if (usedBytes == maxBytes - 1 && (data & mask) != 0) {
                throw new RuntimeException("var int too long for type " + type);
            }

            zigzag |= (long) data << (7 * usedBytes++);
        } while ((b & 0b10000000) != 0);

        long result = (zigzag >>> 1) ^ -(zigzag & 1);
        check(result, inBoundsInclusive(-1L << (bits - 1), (1L << (bits - 1)) - 1));

        return result;
    }

    private void writeVarInt(Type type, long n) {
        check(type.isVarInt(), isTrue());

        int bits = 8 * type.getByteCount();
        check(n, inBoundsInclusive(-1L << (bits - 1), (1L << (bits - 1)) - 1));

        if (debugTypes) {
            writeType(type);
        }

        long zigzag = (n >> (Long.SIZE - 1)) ^ (n << 1);
        do
        {
            int data = (int) (zigzag & 0b01111111);
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
        check(n, inBoundsInclusive(JavaMath.UNSIGNED_SHORT_MAX_VALUE));

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
        check(n, inBoundsInclusive(JavaMath.UNSIGNED_INT_MAX_VALUE));

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
        return JavaMath.toUnsignedBigInteger(readVarInt(Type.VAR_U64));
    }

    @Override
    public void writeLong(long n) {
        writeVarInt(Type.VAR_I64, n);
    }

    @Override
    public void writeUnsignedLong(BigInteger n) {
        check(n, inBoundsInclusive(BigInteger.ZERO, JavaMath.UNSIGNED_LONG_MAX_VALUE));

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

    enum Type implements NetworkEnum<Type> {

        I8("i8", ReadablePacketBuffer::readByte, 1, false, true),
        U8("u8", ReadablePacketBuffer::readUnsignedByte, 1, false, true),
        VAR_I16("i16", ReadablePacketBuffer::readShort, 2, true, true),
        VAR_I32("i32", ReadablePacketBuffer::readInt, 4, true, true),
        VAR_I64("i64", ReadablePacketBuffer::readLong, 8, true, true),
        VAR_U16("u16", ReadablePacketBuffer::readUnsignedShort, 2, true, true),
        VAR_U32("u32", ReadablePacketBuffer::readUnsignedInt, 4, true, true),
        VAR_U64("u64", ReadablePacketBuffer::readUnsignedLong, 8, true, true),
        BOOL("b", ReadablePacketBuffer::readBoolean, 1, false, false),
        STR("s", buf -> "'" + buf.readString() + "'", -1, true, false);

        private final String symbol;
        private final Function<ReadablePacketBuffer, ?> extractor;
        private final int byteCount;
        private final boolean variableLength;
        private final boolean integer;

        Type(String symbol, Function<ReadablePacketBuffer, ?> extractor, int byteCount, boolean variableLength,
             boolean integer) {
            this.symbol = symbol;
            this.extractor = extractor;
            this.byteCount = byteCount;
            this.variableLength = variableLength;
            this.integer = integer;
        }

        public static Type fromId(byte id) {
            Type[] values = values();
            int ordinal = id - 1;
            check(ordinal, inBounds(values.length));

            return values[ordinal];
        }

        @Override
        public byte getId() {
            int id = ordinal() + 1;
            check(id, inBoundsInclusive(Byte.MAX_VALUE));

            return (byte) id;
        }

        public String toString(ReadablePacketBuffer buf) {
            return symbol + ":" + extractor.apply(buf);
        }

        public boolean hasVariableLength() {
            return variableLength;
        }

        public boolean hasDefinedByteCount() {
            return byteCount >= 0;
        }

        public int getByteCount() {
            check(hasDefinedByteCount(), isTrue());

            return byteCount;
        }

        public boolean isInteger() {
            return integer;
        }

        public boolean isVarInt() {
            return hasVariableLength() && isInteger();
        }
    }
}
