package io.github.ititus.skat.network.buffer;

import io.github.ititus.math.number.BigIntegerMath;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static io.github.ititus.skat.util.MathUtil.toUnsignedBigInteger;
import static java.lang.Integer.toUnsignedLong;
import static java.lang.Short.toUnsignedInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class PacketBufferTest {

    PacketBufferImpl pb;

    private static Stream<Arguments> testI16() {
        return Stream.of(
                of((short) 0, 1),
                of((short) -1, 1),
                of((short) 1, 1),
                of((short) 63, 1),
                of((short) -64, 1),

                of((short) 64, 2),
                of((short) -65, 2),
                of((short) 8_191, 2),
                of((short) -8_192, 2),

                of((short) 8_192, 3),
                of((short) -8_193, 3),
                of(Short.MAX_VALUE, 3),
                of(Short.MIN_VALUE, 3)
        );
    }

    private static Stream<Arguments> testU16() {
        return testI16().map(args -> {
            Object[] objs = args.get();
            short n = ((Number) objs[0]).shortValue();
            if (n < 0) {
                return of(toUnsignedInt(n), objs[1]);
            }

            return args;
        });
    }

    private static Stream<Arguments> testI32() {
        return Stream.concat(testI16(), Stream.of(
                of(1_048_575, 3),
                of(-1_048_576, 3),

                of(1_048_576, 4),
                of(-1_048_577, 4),
                of(134_217_727, 4),
                of(-134_217_728, 4),

                of(134_217_728, 5),
                of(-134_217_729, 5),
                of(Integer.MAX_VALUE, 5),
                of(Integer.MIN_VALUE, 5)
        ));
    }

    private static Stream<Arguments> testU32() {
        return testI32().map(args -> {
            Object[] objs = args.get();
            int n = ((Number) objs[0]).intValue();
            if (n < 0) {
                return of(toUnsignedLong(n), objs[1]);
            }

            return args;
        });
    }

    private static Stream<Arguments> testI64() {
        return Stream.concat(testI32(), Stream.of(
                of(17_179_869_183L, 5),
                of(-17_179_869_184L, 5),

                of(17_179_869_184L, 6),
                of(-17_179_869_185L, 6),
                of(2_199_023_255_551L, 6),
                of(-2_199_023_255_552L, 6),

                of(2_199_023_255_552L, 7),
                of(-2_199_023_255_553L, 7),
                of(281_474_976_710_655L, 7),
                of(-281_474_976_710_656L, 7),

                of(281_474_976_710_656L, 8),
                of(-281_474_976_710_657L, 8),
                of(36_028_797_018_963_967L, 8),
                of(-36_028_797_018_963_968L, 8),

                of(36_028_797_018_963_968L, 9),
                of(-36_028_797_018_963_969L, 9),
                of(4_611_686_018_427_387_903L, 9),
                of(-4_611_686_018_427_387_904L, 9),

                of(4_611_686_018_427_387_904L, 10),
                of(-4_611_686_018_427_387_905L, 10),
                of(Long.MAX_VALUE, 10),
                of(Long.MIN_VALUE, 10)
        ));
    }

    private static Stream<Arguments> testU64() {
        return testI64().map(args -> {
            Object[] objs = args.get();
            long n = ((Number) objs[0]).longValue();
            if (n < 0) {
                return of(toUnsignedBigInteger(n), objs[1]);
            }

            return of(BigIntegerMath.of(n), objs[1]);
        });
    }

    @BeforeEach
    void setup() {
        pb = new PacketBufferImpl(Unpooled.buffer(), false);
    }

    @ParameterizedTest
    @ValueSource(bytes = {
            0,
            -1,
            1,
            63,
            -64,
            Byte.MAX_VALUE,
            Byte.MIN_VALUE
    })
    void testI8(byte n) {
        assertThat(pb.readableBytes()).isEqualTo(0);

        pb.writeByte(n);

        assertThat(pb.readableBytes()).isEqualTo(1);
        assertThat(pb.readByte()).isEqualTo(n);
        assertThat(pb.readableBytes()).isEqualTo(0);
    }

    @ParameterizedTest
    @ValueSource(shorts = {
            0,
            1,
            63,
            64,
            127,
            128,
            255
    })
    void testU8(short n) {
        assertThat(pb.readableBytes()).isEqualTo(0);

        pb.writeUnsignedByte(n);

        assertThat(pb.readableBytes()).isEqualTo(1);
        assertThat(pb.readUnsignedByte()).isEqualTo(n);
        assertThat(pb.readableBytes()).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource
    void testI16(short n, int expectedReadableBytes) {
        assertThat(pb.readableBytes()).isEqualTo(0);

        pb.writeShort(n);

        assertThat(pb.readableBytes()).isEqualTo(expectedReadableBytes);
        assertThat(pb.readShort()).isEqualTo(n);
        assertThat(pb.readableBytes()).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource
    void testU16(int n, int expectedReadableBytes) {
        assertThat(pb.readableBytes()).isEqualTo(0);

        pb.writeUnsignedShort(n);

        assertThat(pb.readableBytes()).isEqualTo(expectedReadableBytes);
        assertThat(pb.readUnsignedShort()).isEqualTo(n);
        assertThat(pb.readableBytes()).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource
    void testI32(int n, int expectedReadableBytes) {
        assertThat(pb.readableBytes()).isEqualTo(0);

        pb.writeInt(n);

        assertThat(pb.readableBytes()).isEqualTo(expectedReadableBytes);
        assertThat(pb.readInt()).isEqualTo(n);
        assertThat(pb.readableBytes()).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource
    void testU32(long n, int expectedReadableBytes) {
        assertThat(pb.readableBytes()).isEqualTo(0);

        pb.writeUnsignedInt(n);

        assertThat(pb.readableBytes()).isEqualTo(expectedReadableBytes);
        assertThat(pb.readUnsignedInt()).isEqualTo(n);
        assertThat(pb.readableBytes()).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource
    void testI64(long n, int expectedReadableBytes) {
        assertThat(pb.readableBytes()).isEqualTo(0);

        pb.writeLong(n);

        assertThat(pb.readableBytes()).isEqualTo(expectedReadableBytes);
        assertThat(pb.readLong()).isEqualTo(n);
        assertThat(pb.readableBytes()).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource
    void testU64(BigInteger n, int expectedReadableBytes) {
        assertThat(pb.readableBytes()).isEqualTo(0);

        pb.writeUnsignedLong(n);

        assertThat(pb.readableBytes()).isEqualTo(expectedReadableBytes);
        assertThat(pb.readUnsignedLong()).isEqualTo(n);
        assertThat(pb.readableBytes()).isEqualTo(0);
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void testBool(boolean b) {
        assertThat(pb.readableBytes()).isEqualTo(0);

        pb.writeBoolean(b);

        assertThat(pb.readableBytes()).isEqualTo(1);
        assertThat(pb.readBoolean()).isEqualTo(b);
        assertThat(pb.readableBytes()).isEqualTo(0);
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "a", "ab", "abc", "ä" })
    void testString(String s) {
        assertThat(pb.readableBytes()).isEqualTo(0);

        pb.writeString(s);

        assertThat(pb.readableBytes()).isEqualTo(s.getBytes(StandardCharsets.UTF_8).length + 1);
        assertThat(pb.readString()).isEqualTo(s);
        assertThat(pb.readableBytes()).isEqualTo(0);
    }
}
