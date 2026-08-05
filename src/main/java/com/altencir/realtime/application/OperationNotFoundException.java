package com.altencir.realtime.application;

import java.util.UUID;

public final class OperationNotFoundException extends RuntimeException {
    public OperationNotFoundException(UUID id) { super("Operation '%s' was not found".formatted(id)); }
}
