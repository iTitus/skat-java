package io.github.ititus.skat.util.precondition;

import java.util.function.Function;
import java.util.function.Predicate;

public final class StringPrecondition extends ComparablePrecondition<String> {

    private StringPrecondition(Predicate<String> predicate, Function<String, String> failDescription) {
        super(predicate, failDescription);
    }

    public static StringPrecondition notContains(char c) {
        return indexOf(c, IntPrecondition.equalTo(-1));
    }

    public static StringPrecondition contains(char c) {
        return indexOf(c, IntPrecondition.greaterThanOrEqualTo(0));
    }

    public static StringPrecondition indexOf(char c, IntPrecondition indexPrecondition) {
        return new StringPrecondition(
                value -> value != null && indexPrecondition.test(value.indexOf(c)),
                value -> {
                    String result = "Expected index of " + c + " in " + value + " to match precondition";
                    if (value != null) {
                        result += ": " + indexPrecondition.getFailDescription(value.indexOf(c));
                    } else {
                        result += ", but the string was null";
                    }

                    return result;
                }
        );
    }
}
