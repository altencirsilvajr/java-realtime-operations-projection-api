package com.altencir.realtime.application;

import java.util.Optional;
import java.util.UUID;

public interface OperationProjectionRepository {
    OperationSnapshot create(OperationSnapshot snapshot);
    OperationSnapshot append(OperationSnapshot snapshot);
    Optional<OperationSnapshot> find(UUID id);
}
