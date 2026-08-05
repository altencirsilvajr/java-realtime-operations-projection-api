package com.altencir.realtime.api;

import com.altencir.realtime.application.OperationService;
import com.altencir.realtime.application.OperationSnapshot;
import com.altencir.realtime.domain.OperationStatus;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operations")
public class OperationsController {
    private final OperationService service;
    public OperationsController(OperationService service) { this.service = service; }

    @PostMapping
    @Operation(summary = "Register an operation")
    ResponseEntity<OperationSnapshot> register(@Valid @RequestBody CreateOperationRequest request) {
        var snapshot = service.register(request.name());
        return ResponseEntity.created(URI.create("/api/operations/" + snapshot.id())).body(snapshot);
    }

    @PostMapping("/{id}/transitions")
    @Operation(summary = "Transition an operation")
    OperationSnapshot transition(@PathVariable UUID id, @Valid @RequestBody TransitionOperationRequest request) {
        return service.transition(id, request.status());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Recover the persisted operation snapshot")
    OperationSnapshot snapshot(@PathVariable UUID id) { return service.get(id); }

    record CreateOperationRequest(@NotBlank String name) { }
    record TransitionOperationRequest(@NotNull OperationStatus status) { }
}
