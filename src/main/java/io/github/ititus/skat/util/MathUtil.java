package io.github.ititus.skat.util;

import io.github.ititus.math.number.BigIntegerMath;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;

public final class MathUtil {

    public static final short UNSIGNED_BYTE_MAX_VALUE = (1 << Byte.SIZE) - 1;
    public static final int UNSIGNED_SHORT_MAX_VALUE = (1 << Short.SIZE) - 1;
    public static final long UNSIGNED_INT_MAX_VALUE = (1L << Integer.SIZE) - 1;
    public static final BigInteger UNSIGNED_LONG_MAX_VALUE = ONE.shiftLeft(Long.SIZE).subtract(ONE);

    private MathUtil() {
    }

    public static int ceilDiv(int x, int y) {
        return Math.floorDiv(x, y) + (x % y == 0 ? 0 : 1);
    }

    public static BigInteger toUnsignedBigInteger(long n) {
        return BigIntegerMath.of(n).and(UNSIGNED_LONG_MAX_VALUE);
    }
}
