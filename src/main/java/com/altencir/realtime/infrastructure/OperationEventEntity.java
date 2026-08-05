package com.altencir.realtime.infrastructure;

import com.altencir.realtime.domain.OperationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity @Table(name = "operation_event")
class OperationEventEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "operation_id") OperationProjectionEntity operation;
    long sequence;
    @Enumerated(EnumType.STRING) OperationStatus previousStatus;
    @Enumerated(EnumType.STRING) OperationStatus newStatus;
    Instant occurredAt;

    protected OperationEventEntity() { }
}
