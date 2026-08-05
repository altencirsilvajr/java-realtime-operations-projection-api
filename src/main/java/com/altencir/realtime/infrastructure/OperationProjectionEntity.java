package com.altencir.realtime.infrastructure;

import com.altencir.realtime.domain.OperationStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity @Table(name = "operation_projection")
class OperationProjectionEntity {
    @Id UUID id;
    String name;
    @Enumerated(EnumType.STRING) OperationStatus status;
    long operationVersion;
    Instant createdAt;
    Instant lastChangedAt;
    @Version long lockVersion;
    @OneToMany(mappedBy = "operation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("sequence ASC")
    List<OperationEventEntity> timeline = new ArrayList<>();

    protected OperationProjectionEntity() { }
}
