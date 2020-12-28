package io.github.ititus.skat.network.buffer;

import io.github.ititus.math.number.BigIntegerMath;
import io.github.ititus.skat.util.MathUtil;
import io.netty.buffer.ByteBuf;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class PacketBufferImpl implements ReadablePacketBuffer, WritablePacketBuffer {

    private static final boolean DEBUG_TYPES = true;
    private static final BigInteger DATA_MASK = BigIntegerMath.of(0b01111111);

    private final ByteBuf buf;

    public PacketBufferImpl(ByteBuf buf) {
        this.buf = buf;
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
        if (DEBUG_TYPES) {
            checkType(Type.BOOL);
        }

        return buf.readBoolean();
    }

    @Override
    public void writeBoolean(boolean b) {
        if (DEBUG_TYPES) {
            writeType(Type.BOOL);
        }

        buf.writeBoolean(b);
    }

    @Override
    public String readString() {
        if (DEBUG_TYPES) {
            checkType(Type.STR);
        }

        buf.markReaderIndex();
        int len = 0;
        while (buf.readByte() != 0) {
            len++;
        }

        buf.resetReaderIndex();

        byte[] bytes = new byte[len];
        buf.readBytes(bytes);

        String str = new String(bytes, StandardCharsets.UTF_8);

        buf.readByte();
        return str;
    }

    @Override
    public void writeString(String s) {
        if (DEBUG_TYPES) {
            writeType(Type.STR);
        }

        buf.writeBytes(s.getBytes(StandardCharsets.UTF_8));
        buf.writeByte(0);
    }

    @Override
    public byte readByte() {
        if (DEBUG_TYPES) {
            checkType(Type.I8);
        }

        return buf.readByte();
    }

    @Override
    public short readUnsignedByte() {
        if (DEBUG_TYPES) {
            checkType(Type.U8);
        }

        return buf.readUnsignedByte();
    }

    @Override
    public void writeByte(byte n) {
        if (DEBUG_TYPES) {
            writeType(Type.I8);
        }

        buf.writeByte(n);
    }

    @Override
    public void writeUnsignedByte(short n) {
        if (DEBUG_TYPES) {
            writeType(Type.U8);
        }

        buf.writeByte(n);
    }

    private BigInteger readVarInt(Type type) {
        if (!type.isVarInt()) {
            throw new IllegalArgumentException("given type " + type + " is not a var int type");
        }

        if (DEBUG_TYPES) {
            checkType(type);
        }

        int bits = type.bitCount();
        int maxBytes = MathUtil.ceilDiv(bits, 7);
        int superfluousBits = (maxBytes * 7) - bits;
        int mask = ((byte) -1) << (7 - superfluousBits);
        BigInteger zigzag = ZERO;

        int usedBytes = 0;
        byte b;
        do {
            b = buf.readByte();
            int data = b & 0b01111111;
            if (usedBytes == maxBytes - 1 && (data & mask) != 0) {
                throw new RuntimeException("var int too big");
            }

            zigzag = zigzag.or(BigIntegerMath.of(data).shiftLeft(7 * usedBytes++));
        } while ((b & 0b10000000) != 0);

        return zigzag.shiftRight(1).xor(zigzag.and(ONE).negate());
    }

    private void writeVarInt(Type type, BigInteger n) {
        if (!type.isVarInt()) {
            throw new IllegalArgumentException("given type " + type + " is not a var int type");
        } else if (type.isUnsigned() && n.signum() < 0) {
            throw new IllegalArgumentException("given var int type is unsigned, but the given integer is negative");
        }

        if (DEBUG_TYPES) {
            writeType(type);
        }

        int bits = type.bitCount();

        int usableBits = type.isUnsigned() ? bits : bits - 1;
        if (n.bitLength() > usableBits) {
            throw new RuntimeException("number too big");
        }

        BigInteger zigzag = n.shiftRight(bits - 1).xor(n.shiftLeft(1));
        do {
            int data = zigzag.and(DATA_MASK).intValue();
            zigzag = zigzag.shiftRight(7);
            if (zigzag.signum() > 0) {
                data |= 0b10000000;
            }

            buf.writeByte(data);
        } while (zigzag.signum() > 0);
    }

    @Override
    public short readShort() {
        return readVarInt(Type.VAR_I16).shortValue();
    }

    @Override
    public int readUnsignedShort() {
        return readVarInt(Type.VAR_U16).intValue();
    }

    @Override
    public void writeShort(short n) {
        writeVarInt(Type.VAR_I16, BigIntegerMath.of(n));
    }

    @Override
    public void writeUnsignedShort(int n) {
        writeVarInt(Type.VAR_U16, BigIntegerMath.of(n));
    }

    @Override
    public int readInt() {
        return readVarInt(Type.VAR_I32).intValue();
    }

    @Override
    public long readUnsignedInt() {
        return readVarInt(Type.VAR_U32).longValue();
    }

    @Override
    public void writeInt(int n) {
        writeVarInt(Type.VAR_I32, BigIntegerMath.of(n));
    }

    @Override
    public void writeUnsignedInt(long n) {
        writeVarInt(Type.VAR_U32, BigIntegerMath.of(n));
    }

    @Override
    public long readLong() {
        return readVarInt(Type.VAR_I64).longValue();
    }

    @Override
    public BigInteger readUnsignedLong() {
        return readVarInt(Type.VAR_U64);
    }

    @Override
    public void writeLong(long n) {
        writeVarInt(Type.VAR_I64, BigIntegerMath.of(n));
    }

    @Override
    public void writeUnsignedLong(BigInteger n) {
        writeVarInt(Type.VAR_U64, n);
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

        buf.markReaderIndex();
        buf.readerIndex(0);
        if (DEBUG_TYPES) {
            while (buf.isReadable()) {
                Type type = Type.fromId(buf.getByte(buf.readerIndex()));
                b.append(' ').append(type.toString(this));
            }
        } else {
            while (buf.isReadable()) {
                b.append(String.format(" %02x", buf.readUnsignedByte()));
            }
        }
        buf.resetReaderIndex();

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
                    throw new IllegalArgumentException("unknown type id");
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
                case I8:
                case U8:
                case BOOL:
                case STR:
                    return false;
                default:
                    return true;
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
                    throw new RuntimeException("type is not an integer");
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
                    throw new RuntimeException("type is not an integer");
            }
        }
    }
}
