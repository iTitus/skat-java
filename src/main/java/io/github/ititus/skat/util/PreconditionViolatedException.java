package io.github.ititus.skat.util;

public class PreconditionViolatedException extends RuntimeException {

    public PreconditionViolatedException() {
        super();
    }

    public PreconditionViolatedException(String message) {
        super(message);
    }
}
