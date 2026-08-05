package com.altencir.realtime.realtime;

import com.altencir.realtime.application.ProjectionCommitted;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
class StompProjectionPublisher {
    private final SimpMessagingTemplate messaging;
    StompProjectionPublisher(SimpMessagingTemplate messaging) { this.messaging = messaging; }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publish(ProjectionCommitted event) {
        messaging.convertAndSend("/topic/operations", event.snapshot());
        messaging.convertAndSend("/topic/operations/" + event.snapshot().id(), event.snapshot());
    }
}
