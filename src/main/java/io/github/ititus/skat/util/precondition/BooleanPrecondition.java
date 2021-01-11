package io.github.ititus.skat.util.precondition;

import io.github.ititus.skat.util.function.BooleanFunction;
import io.github.ititus.skat.util.function.BooleanPredicate;

public final class BooleanPrecondition {

    private final BooleanPredicate predicate;
    private final BooleanFunction<String> failDescription;

    BooleanPrecondition(BooleanPredicate predicate, BooleanFunction<String> failDescription) {
        this.predicate = predicate;
        this.failDescription = failDescription;
    }

    public static BooleanPrecondition isTrue() {
        return equalTo(true);
    }

    public static BooleanPrecondition isFalse() {
        return equalTo(false);
    }

    public static BooleanPrecondition equalTo(boolean other) {
        return new BooleanPrecondition(
                value -> value == other,
                value -> "Expected " + value + " to be equal to " + other
        );
    }

    public static BooleanPrecondition notEqualTo(boolean other) {
        return new BooleanPrecondition(
                value -> value != other,
                value -> "Expected " + value + " to not be equal to " + other
        );
    }

    public boolean test(boolean value) {
        return predicate.test(value);
    }

    public String getFailDescription(boolean value) {
        return failDescription.apply(value);
    }
}
