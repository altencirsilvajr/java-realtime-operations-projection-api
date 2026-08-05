package com.altencir.realtime.infrastructure;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaProjectionRepository extends JpaRepository<OperationProjectionEntity, UUID> { }
