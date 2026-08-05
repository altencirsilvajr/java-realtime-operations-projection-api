package com.altencir.realtime.application;

import com.altencir.realtime.domain.OperationStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OperationSnapshot(UUID id, String name, OperationStatus status, long version, Instant createdAt, Instant lastChangedAt, List<TimelineEntry> timeline) {
    public record TimelineEntry(long sequence, OperationStatus previousStatus, OperationStatus newStatus, Instant occurredAt) { }
}
