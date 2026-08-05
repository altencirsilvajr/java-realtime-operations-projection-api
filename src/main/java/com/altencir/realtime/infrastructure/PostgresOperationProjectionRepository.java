package com.altencir.realtime.infrastructure;

import com.altencir.realtime.application.OperationProjectionRepository;
import com.altencir.realtime.application.OperationSnapshot;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
class PostgresOperationProjectionRepository implements OperationProjectionRepository {
    private final JpaProjectionRepository repository;
    PostgresOperationProjectionRepository(JpaProjectionRepository repository) { this.repository = repository; }

    public OperationSnapshot create(OperationSnapshot snapshot) {
        var entity = new OperationProjectionEntity();
        entity.id = snapshot.id(); entity.name = snapshot.name(); entity.status = snapshot.status();
        entity.operationVersion = snapshot.version(); entity.createdAt = snapshot.createdAt(); entity.lastChangedAt = snapshot.lastChangedAt();
        addEvent(entity, snapshot.timeline().getFirst());
        return toSnapshot(repository.saveAndFlush(entity));
    }

    public OperationSnapshot append(OperationSnapshot snapshot) {
        var entity = repository.findById(snapshot.id()).orElseThrow();
        entity.status = snapshot.status(); entity.operationVersion = snapshot.version(); entity.lastChangedAt = snapshot.lastChangedAt();
        addEvent(entity, snapshot.timeline().getLast());
        return toSnapshot(repository.saveAndFlush(entity));
    }

    public Optional<OperationSnapshot> find(UUID id) { return repository.findById(id).map(this::toSnapshot); }

    private void addEvent(OperationProjectionEntity entity, OperationSnapshot.TimelineEntry entry) {
        var event = new OperationEventEntity();
        event.operation = entity; event.sequence = entry.sequence(); event.previousStatus = entry.previousStatus();
        event.newStatus = entry.newStatus(); event.occurredAt = entry.occurredAt(); entity.timeline.add(event);
    }

    private OperationSnapshot toSnapshot(OperationProjectionEntity entity) {
        var timeline = entity.timeline.stream().map(e -> new OperationSnapshot.TimelineEntry(e.sequence, e.previousStatus, e.newStatus, e.occurredAt)).toList();
        return new OperationSnapshot(entity.id, entity.name, entity.status, entity.operationVersion, entity.createdAt, entity.lastChangedAt, timeline);
    }
}
