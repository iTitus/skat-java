package io.github.ititus.skat.util.function;

@FunctionalInterface
public interface BooleanFunction<R> {

    R apply(boolean value);

}
