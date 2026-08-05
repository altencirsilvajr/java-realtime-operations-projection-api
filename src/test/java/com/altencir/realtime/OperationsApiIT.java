package com.altencir.realtime;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OperationsApiIT {
    @Container @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @LocalServerPort int port;
    @Autowired TestRestTemplate rest;

    @Test
    void persistsAnOrderedSnapshotBeforePublishingRealtimeUpdate() throws Exception {
        var messages = new ArrayBlockingQueue<Map<String, Object>>(1);
        var stomp = new WebSocketStompClient(new StandardWebSocketClient());
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        var session = stomp.connectAsync("ws://localhost:%d/ws".formatted(port), new org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter() {}).get();
        session.subscribe("/topic/operations", mapHandler(messages));

        var created = rest.postForEntity("/api/operations", Map.of("name", "Import payroll"), Map.class);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        var pushed = messages.poll(Duration.ofSeconds(5).toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS);
        assertThat(pushed).isNotNull();

        var id = UUID.fromString(pushed.get("id").toString());
        var persisted = rest.getForEntity("/api/operations/{id}", Map.class, id);
        assertThat(persisted.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(persisted.getBody()).containsEntry("version", 1);
        assertThat(persisted.getBody()).containsEntry("status", "CREATED");
        session.disconnect();
    }

    @Test
    void reconnectsThroughSnapshotWithCompleteOrderedTimeline() {
        var created = rest.postForEntity("/api/operations", Map.of("name", "Settle invoices"), Map.class);
        var id = created.getBody().get("id");
        rest.postForEntity("/api/operations/{id}/transitions", Map.of("status", "PROCESSING"), Map.class, id);
        rest.postForEntity("/api/operations/{id}/transitions", Map.of("status", "COMPLETED"), Map.class, id);

        var snapshot = rest.getForEntity("/api/operations/{id}", Map.class, id);
        assertThat(snapshot.getBody()).containsEntry("version", 3).containsEntry("status", "COMPLETED");
        var timeline = (java.util.List<Map<String, Object>>) snapshot.getBody().get("timeline");
        assertThat(timeline).extracting(e -> e.get("sequence")).containsExactly(1, 2, 3);
    }

    @Test
    void rejectsInvalidTransitionsWithProblemDetails() {
        var created = rest.postForEntity("/api/operations", Map.of("name", "Settle invoices"), Map.class);
        var id = created.getBody().get("id");
        var response = rest.postForEntity("/api/operations/{id}/transitions", Map.of("status", "COMPLETED"), Map.class, id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).containsEntry("title", "Invalid operation transition");
    }

    private StompFrameHandler mapHandler(ArrayBlockingQueue<Map<String, Object>> messages) {
        return new StompFrameHandler() {
            public Type getPayloadType(StompHeaders headers) { return Map.class; }
            public void handleFrame(StompHeaders headers, Object payload) { messages.add((Map<String, Object>) payload); }
        };
    }
}
