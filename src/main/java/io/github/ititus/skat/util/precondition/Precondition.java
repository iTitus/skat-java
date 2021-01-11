package io.github.ititus.skat.util.precondition;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class Precondition<T> {

    private final Predicate<T> predicate;
    private final Function<T, String> failDescription;

    protected Precondition(Predicate<T> predicate, Function<T, String> failDescription) {
        this.predicate = predicate;
        this.failDescription = failDescription;
    }

    public static <T> Precondition<T> isNull() {
        return equalTo(null);
    }

    public static <T> Precondition<T> equalTo(T other) {
        return new Precondition<>(
                value -> Objects.equals(value, other),
                value -> "Expected " + value + " to be equal to " + other
        );
    }

    public static <T> Precondition<T> notNull() {
        return notEqualTo(null);
    }

    public static <T> Precondition<T> notEqualTo(T other) {
        return new Precondition<>(
                value -> !Objects.equals(value, other),
                value -> "Expected " + value + " to not be equal to " + other
        );
    }

    public boolean test(T value) {
        return predicate.test(value);
    }

    public String getFailDescription(T value) {
        return failDescription.apply(value);
    }
}
