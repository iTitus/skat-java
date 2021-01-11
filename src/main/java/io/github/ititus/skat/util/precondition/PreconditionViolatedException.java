package io.github.ititus.skat.util.precondition;

public final class PreconditionViolatedException extends RuntimeException {

    PreconditionViolatedException() {
        super();
    }

    PreconditionViolatedException(String message) {
        super(message);
    }
}
