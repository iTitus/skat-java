package io.github.ititus.skat.util.precondition;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class ComparablePrecondition<T extends Comparable<T>> extends Precondition<T> {

    protected ComparablePrecondition(Predicate<T> predicate, Function<T, String> failDescription) {
        super(predicate, failDescription);
    }

    public static <T extends Comparable<T>> ComparablePrecondition<T> lessThan(T other) {
        Objects.requireNonNull(other);
        return new ComparablePrecondition<>(
                value -> value != null && value.compareTo(other) < 0,
                value -> "Expected " + value + " to be less than " + other
        );
    }

    public static <T extends Comparable<T>> ComparablePrecondition<T> lessThanOrEqualTo(T other) {
        Objects.requireNonNull(other);
        return new ComparablePrecondition<>(
                value -> value != null && value.compareTo(other) <= 0,
                value -> "Expected " + value + " to be less than or equal to " + other
        );
    }

    public static <T extends Comparable<T>> ComparablePrecondition<T> greaterThan(T other) {
        Objects.requireNonNull(other);
        return new ComparablePrecondition<>(
                value -> value != null && value.compareTo(other) > 0,
                value -> "Expected " + value + " to be greater than " + other
        );
    }

    public static <T extends Comparable<T>> ComparablePrecondition<T> greaterThanOrEqualTo(T other) {
        Objects.requireNonNull(other);
        return new ComparablePrecondition<>(
                value -> value != null && value.compareTo(other) >= 0,
                value -> "Expected " + value + " to be greater than or equal to " + other
        );
    }

    public static <T extends Comparable<T>> ComparablePrecondition<T> inBounds(T lower, T upper) {
        Objects.requireNonNull(lower);
        Objects.requireNonNull(upper);
        return new ComparablePrecondition<>(
                value -> value != null && lower.compareTo(value) <= 0 && value.compareTo(upper) < 0,
                value -> "Expected " + value + " to be in the interval [" + lower + ", " + upper + ")"
        );
    }

    public static <T extends Comparable<T>> ComparablePrecondition<T> inBoundsInclusive(T lower, T upper) {
        Objects.requireNonNull(lower);
        Objects.requireNonNull(upper);
        return new ComparablePrecondition<>(
                value -> value != null && lower.compareTo(value) <= 0 && value.compareTo(upper) <= 0,
                value -> "Expected " + value + " to be in the interval [" + lower + ", " + upper + "]"
        );
    }
}
