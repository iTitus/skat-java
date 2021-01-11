package io.github.ititus.skat.util.precondition;

import java.util.function.IntFunction;
import java.util.function.IntPredicate;

public final class IntPrecondition {

    private final IntPredicate predicate;
    private final IntFunction<String> failDescription;

    IntPrecondition(IntPredicate predicate, IntFunction<String> failDescription) {
        this.predicate = predicate;
        this.failDescription = failDescription;
    }

    public static IntPrecondition zero() {
        return equalTo(0);
    }

    public static IntPrecondition equalTo(int other) {
        return new IntPrecondition(
                value -> value == other,
                value -> "Expected " + value + " to be equal to " + other
        );
    }

    public static IntPrecondition notZero() {
        return notEqualTo(0);
    }

    public static IntPrecondition notEqualTo(int other) {
        return new IntPrecondition(
                value -> value != other,
                value -> "Expected " + value + " to not be equal to " + other
        );
    }

    public static IntPrecondition lessThan(int other) {
        return new IntPrecondition(
                value -> value < other,
                value -> "Expected " + value + " to be less than " + other
        );
    }

    public static IntPrecondition lessThanOrEqualTo(int other) {
        return new IntPrecondition(
                value -> value <= other,
                value -> "Expected " + value + " to be less than or equal to " + other
        );
    }

    public static IntPrecondition greaterThan(int other) {
        return new IntPrecondition(
                value -> value > other,
                value -> "Expected " + value + " to be greater than " + other
        );
    }

    public static IntPrecondition greaterThanOrEqualTo(int other) {
        return new IntPrecondition(
                value -> value >= other,
                value -> "Expected " + value + " to be greater than or equal to " + other
        );
    }

    public static IntPrecondition inBounds(int upper) {
        return inBounds(0, upper);
    }

    public static IntPrecondition inBounds(int lower, int upper) {
        return new IntPrecondition(
                value -> lower <= value && value < upper,
                value -> "Expected " + value + " to be in the interval [" + lower + ", " + upper + ")"
        );
    }

    public static IntPrecondition inBoundsInclusive(int upper) {
        return inBoundsInclusive(0, upper);
    }

    public static IntPrecondition inBoundsInclusive(int lower, int upper) {
        return new IntPrecondition(
                value -> lower <= value && value <= upper,
                value -> "Expected " + value + " to be in the interval [" + lower + ", " + upper + "]"
        );
    }

    public boolean test(int value) {
        return predicate.test(value);
    }

    public String getFailDescription(int value) {
        return failDescription.apply(value);
    }
}
