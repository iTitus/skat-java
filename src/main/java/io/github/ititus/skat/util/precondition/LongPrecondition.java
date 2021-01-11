package io.github.ititus.skat.util.precondition;

import java.util.function.LongFunction;
import java.util.function.LongPredicate;

public final class LongPrecondition {

    private final LongPredicate predicate;
    private final LongFunction<String> failDescription;

    LongPrecondition(LongPredicate predicate, LongFunction<String> failDescription) {
        this.predicate = predicate;
        this.failDescription = failDescription;
    }

    public static LongPrecondition zero() {
        return equalTo(0);
    }

    public static LongPrecondition equalTo(long other) {
        return new LongPrecondition(
                value -> value == other,
                value -> "Expected " + value + " to be equal to " + other
        );
    }

    public static LongPrecondition notZero() {
        return notEqualTo(0);
    }

    public static LongPrecondition notEqualTo(long other) {
        return new LongPrecondition(
                value -> value != other,
                value -> "Expected " + value + " to not be equal to " + other
        );
    }

    public static LongPrecondition lessThan(long other) {
        return new LongPrecondition(
                value -> value < other,
                value -> "Expected " + value + " to be less than " + other
        );
    }

    public static LongPrecondition lessThanOrEqualTo(long other) {
        return new LongPrecondition(
                value -> value <= other,
                value -> "Expected " + value + " to be less than or equal to " + other
        );
    }

    public static LongPrecondition greaterThan(long other) {
        return new LongPrecondition(
                value -> value > other,
                value -> "Expected " + value + " to be greater than " + other
        );
    }

    public static LongPrecondition greaterThanOrEqualTo(long other) {
        return new LongPrecondition(
                value -> value >= other,
                value -> "Expected " + value + " to be greater than or equal to " + other
        );
    }

    public static LongPrecondition inBounds(long upper) {
        return inBounds(0, upper);
    }

    public static LongPrecondition inBounds(long lower, long upper) {
        return new LongPrecondition(
                value -> lower <= value && value < upper,
                value -> "Expected " + value + " to be in the interval [" + lower + ", " + upper + ")"
        );
    }

    public static LongPrecondition inBoundsInclusive(long upper) {
        return inBoundsInclusive(0, upper);
    }

    public static LongPrecondition inBoundsInclusive(long lower, long upper) {
        return new LongPrecondition(
                value -> lower <= value && value <= upper,
                value -> "Expected " + value + " to be in the interval [" + lower + ", " + upper + "]"
        );
    }

    public boolean test(long value) {
        return predicate.test(value);
    }

    public String getFailDescription(long value) {
        return failDescription.apply(value);
    }
}
