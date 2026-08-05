package com.altencir.realtime.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Operation {
    private final UUID id;
    private final String name;
    private OperationStatus status;
    private long version;
    private final List<OperationStatusChanged> events = new ArrayList<>();

    private Operation(UUID id, String name, OperationStatus status, long version) {
        this.id = Objects.requireNonNull(id);
        this.name = requireName(name);
        this.status = Objects.requireNonNull(status);
        this.version = version;
    }

    public static Operation register(UUID id, String name, Instant occurredAt) {
        var operation = new Operation(id, name, OperationStatus.CREATED, 0);
        operation.record(null, OperationStatus.CREATED, occurredAt);
        return operation;
    }

    public static Operation rehydrate(UUID id, String name, OperationStatus status, long version) {
        return new Operation(id, name, status, version);
    }

    public void transitionTo(OperationStatus next, Instant occurredAt) {
        if (!canTransitionTo(next)) {
            throw new InvalidOperationTransitionException(status, next);
        }
        var previous = status;
        status = next;
        record(previous, next, occurredAt);
    }

    private boolean canTransitionTo(OperationStatus next) {
        return status == OperationStatus.CREATED && next == OperationStatus.PROCESSING
                || status == OperationStatus.PROCESSING && (next == OperationStatus.COMPLETED || next == OperationStatus.FAILED);
    }

    private void record(OperationStatus previous, OperationStatus next, Instant occurredAt) {
        version++;
        events.add(new OperationStatusChanged(id, version, previous, next, Objects.requireNonNull(occurredAt)));
    }

    private static String requireName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Operation name is required");
        }
        return name.trim();
    }

    public UUID id() { return id; }
    public String name() { return name; }
    public OperationStatus status() { return status; }
    public long version() { return version; }
    public List<OperationStatusChanged> events() { return List.copyOf(events); }
}
