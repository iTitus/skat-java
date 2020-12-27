package io.github.ititus.skat.util;

public final class MathUtil {

    private MathUtil() {
    }

    public static int ceilDiv(int x, int y) {
        return Math.floorDiv(x, y) + (x % y == 0 ? 0 : 1);
    }
}
