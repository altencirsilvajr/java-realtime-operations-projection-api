package com.altencir.realtime.domain;

import java.time.Instant;
import java.util.UUID;

public record OperationStatusChanged(
        UUID operationId,
        long sequence,
        OperationStatus previousStatus,
        OperationStatus newStatus,
        Instant occurredAt) {
}
