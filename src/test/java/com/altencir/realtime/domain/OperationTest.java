package com.altencir.realtime.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OperationTest {
    @Test
    void registersAnOperationInCreatedState() {
        var operation = Operation.register(UUID.fromString("b06d65b4-9be0-46bd-b5ab-ce8617863644"), "Import payroll", Instant.parse("2026-08-05T12:00:00Z"));

        assertThat(operation.status()).isEqualTo(OperationStatus.CREATED);
        assertThat(operation.events()).singleElement().satisfies(event -> {
            assertThat(event.sequence()).isEqualTo(1L);
            assertThat(event.previousStatus()).isNull();
            assertThat(event.newStatus()).isEqualTo(OperationStatus.CREATED);
        });
    }

    @Test
    void allowsOnlyForwardOperationalTransitions() {
        var operation = Operation.rehydrate(UUID.randomUUID(), "Import payroll", OperationStatus.CREATED, 1L);
        operation.transitionTo(OperationStatus.PROCESSING, Instant.parse("2026-08-05T12:01:00Z"));
        operation.transitionTo(OperationStatus.COMPLETED, Instant.parse("2026-08-05T12:02:00Z"));

        assertThat(operation.status()).isEqualTo(OperationStatus.COMPLETED);
        assertThat(operation.version()).isEqualTo(3L);
        assertThatThrownBy(() -> operation.transitionTo(OperationStatus.PROCESSING, Instant.now()))
                .isInstanceOf(InvalidOperationTransitionException.class);
    }
}
