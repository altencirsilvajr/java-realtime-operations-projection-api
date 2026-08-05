package com.altencir.realtime.application;

import com.altencir.realtime.domain.Operation;
import com.altencir.realtime.domain.OperationStatus;
import java.time.Clock;
import java.util.List;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OperationService {
    private final OperationProjectionRepository repository;
    private final ApplicationEventPublisher events;
    private final Clock clock;

    public OperationService(OperationProjectionRepository repository, ApplicationEventPublisher events, Clock clock) {
        this.repository = repository;
        this.events = events;
        this.clock = clock;
    }

    @Transactional
    public OperationSnapshot register(String name) {
        var operation = Operation.register(UUID.randomUUID(), name, clock.instant());
        var event = operation.events().getFirst();
        var snapshot = new OperationSnapshot(operation.id(), operation.name(), operation.status(), operation.version(), event.occurredAt(), event.occurredAt(),
                List.of(new OperationSnapshot.TimelineEntry(event.sequence(), event.previousStatus(), event.newStatus(), event.occurredAt())));
        var persisted = repository.create(snapshot);
        events.publishEvent(new ProjectionCommitted(persisted));
        return persisted;
    }

    @Transactional
    public OperationSnapshot transition(UUID id, OperationStatus next) {
        var current = get(id);
        var operation = Operation.rehydrate(current.id(), current.name(), current.status(), current.version());
        operation.transitionTo(next, clock.instant());
        var event = operation.events().getFirst();
        var timeline = new java.util.ArrayList<>(current.timeline());
        timeline.add(new OperationSnapshot.TimelineEntry(event.sequence(), event.previousStatus(), event.newStatus(), event.occurredAt()));
        var snapshot = new OperationSnapshot(id, current.name(), operation.status(), operation.version(), current.createdAt(), event.occurredAt(), List.copyOf(timeline));
        var persisted = repository.append(snapshot);
        events.publishEvent(new ProjectionCommitted(persisted));
        return persisted;
    }

    @Transactional(readOnly = true)
    public OperationSnapshot get(UUID id) {
        return repository.find(id).orElseThrow(() -> new OperationNotFoundException(id));
    }
}
