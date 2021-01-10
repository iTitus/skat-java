package io.github.ititus.skat.util;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class Precondition {

    public static void check(boolean b) {
        actuallyCheck(() -> b, () -> "check failed");
    }

    public static void check(boolean b, String s) {
        actuallyCheck(() -> b, () -> s);
    }

    public static void checkNull(Object o) {
        actuallyCheck(() -> eq().test(o, null), () -> "Unexpected non-null");
    }

    public static void checkNonNull(Object o) {
        actuallyCheck(() -> ne().test(o, null), () -> "Unexpected null");
    }

    public static void checkEq(byte i1, byte i2) {
        actuallyCheck(() -> eq().test(i1, i2), () -> "Not equals: " + i1 + " != " + i2);
    }

    public static void checkEq(short i1, short i2) {
        actuallyCheck(() -> eq().test(i1, i2), () -> "Not equals: " + i1 + " != " + i2);
    }

    public static void checkEq(int i1, int i2) {
        actuallyCheck(() -> eq().test(i1, i2), () -> "Not equals: " + i1 + " != " + i2);
    }

    public static void checkEq(long i1, long i2) {
        actuallyCheck(() -> eq().test(i1, i2), () -> "Not equals: " + i1 + " != " + i2);
    }

    public static void checkEq(Object o1, Object o2) {
        actuallyCheck(() -> eq().test(o1, o2), () -> "Not equals: " + o1 + " != " + o2);
    }

    public static void checkNe(byte i1, byte i2) {
        actuallyCheck(() -> ne().test(i1, i2), () -> "Equals: " + i1 + " == " + i2);
    }

    public static void checkNe(short i1, short i2) {
        actuallyCheck(() -> ne().test(i1, i2), () -> "Equals: " + i1 + " == " + i2);
    }

    public static void checkNe(int i1, int i2) {
        actuallyCheck(() -> ne().test(i1, i2), () -> "Equals: " + i1 + " == " + i2);
    }

    public static void checkNe(long i1, long i2) {
        actuallyCheck(() -> ne().test(i1, i2), () -> "Equals: " + i1 + " == " + i2);
    }

    public static void checkNe(Object o1, Object o2) {
        actuallyCheck(() -> ne().test(o1, o2), () -> "Equals: " + o1 + " == " + o2);
    }

    public static <T extends Comparable<T>> Cmp<T, T> lt() {
        return new Cmp<>("lt", (o1, o2) -> o1.compareTo(o2) < 0);
    }

    public static <T extends Comparable<T>> Cmp<T, T> le() {
        return new Cmp<>("le", (o1, o2) -> o1.compareTo(o2) <= 0);
    }

    public static <T, S> Cmp<T, S> eq() {
        return new Cmp<>("eq", Objects::equals);
    }

    public static <T, S> Cmp<T, S> ne() {
        return new Cmp<>("ne", (o1, o2) -> !Objects.equals(o1, o2));
    }

    public static <T extends Comparable<T>> Cmp<T, T> gt() {
        return new Cmp<>("gt", (o1, o2) -> o1.compareTo(o2) > 0);
    }

    public static <T extends Comparable<T>> Cmp<T, T> ge() {
        return new Cmp<>("ge", (o1, o2) -> o1.compareTo(o2) >= 0);
    }

    public static <T, S> void check(T o1, S o2, BiPredicate<T, S> bp) {
        actuallyCheck(() -> bp.test(o1, o2), () -> "BiPredicate check failed: " + o1 + " and " + o2);
    }

    public static <T, S> void check(T o1, S o2, Cmp<T, S> cmp) {
        actuallyCheck(() -> cmp.test(o1, o2), () -> "BiPredicate check failed: " + o1 + " " + cmp.getName() + " " + o2);
    }

    public static void check(byte o1, byte o2, Cmp<Byte, Byte> cmp) {
        actuallyCheck(() -> cmp.test(o1, o2), () -> "BiPredicate check failed: " + o1 + " " + cmp.getName() + " " + o2);
    }

    public static void check(short o1, short o2, Cmp<Short, Short> cmp) {
        actuallyCheck(() -> cmp.test(o1, o2), () -> "BiPredicate check failed: " + o1 + " " + cmp.getName() + " " + o2);
    }

    public static void check(int o1, int o2, Cmp<Integer, Integer> cmp) {
        actuallyCheck(() -> cmp.test(o1, o2), () -> "BiPredicate check failed: " + o1 + " " + cmp.getName() + " " + o2);
    }

    public static void check(long o1, long o2, Cmp<Long, Long> cmp) {
        actuallyCheck(() -> cmp.test(o1, o2), () -> "BiPredicate check failed: " + o1 + " " + cmp.getName() + " " + o2);
    }

    public static void checkBounds(int val, int lower, int upper) {
        actuallyCheck(() -> lower >= val || val < upper,
                () -> "Bounds check failed: " + lower + " <= " + val + " < " + upper);
    }

    public static void checkBounds(long val, long lower, long upper) {
        actuallyCheck(() -> lower >= val || val < upper,
                () -> "Bounds check failed: " + lower + " <= " + val + " < " + upper);
    }

    public static <T extends Comparable<T>> void checkBounds(T val, T lower, T upper) {
        actuallyCheck(() -> val.compareTo(lower) >= 0 || val.compareTo(upper) < 0,
                () -> "Bounds check failed: " + lower + " <= " + val + " < " + upper);
    }

    public static void checkBoundsI(int val, int lower, int upper) {
        actuallyCheck(() -> lower >= val || val <= upper,
                () -> "Bounds check failed: " + lower + " <= " + val + " <= " + upper);
    }

    public static void checkBoundsI(long val, long lower, long upper) {
        actuallyCheck(() -> lower >= val || val <= upper,
                () -> "Bounds check failed: " + lower + " <= " + val + " <= " + upper);
    }

    public static <T extends Comparable<T>> void checkBoundsI(T val, T lower, T upper) {
        actuallyCheck(() -> val.compareTo(lower) >= 0 || val.compareTo(upper) <= 0,
                () -> "Bounds check failed: " + lower + " <= " + val + " <= " + upper);
    }

    private static void actuallyCheck(BooleanSupplier check, Supplier<String> ss) {
        if (!check.getAsBoolean()) throw new PreconditionViolatedException("Precondition failed: " + ss.get());
    }

    public static class Cmp<T, S> {

        private final String name;
        private final BiPredicate<T, S> bp;

        Cmp(String name, BiPredicate<T, S> bp) {
            this.name = name;
            this.bp = bp;
        }

        public String getName() {
            return name;
        }

        public boolean test(T t, S s) {
            return bp.test(t, s);
        }
    }
}
