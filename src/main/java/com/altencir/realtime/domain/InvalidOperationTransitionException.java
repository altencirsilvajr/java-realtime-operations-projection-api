package com.altencir.realtime.domain;

public final class InvalidOperationTransitionException extends RuntimeException {
    public InvalidOperationTransitionException(OperationStatus current, OperationStatus next) {
        super("Operation cannot transition from %s to %s".formatted(current, next));
    }
}
