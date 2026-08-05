CREATE TABLE operation_projection (
    id UUID PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    status VARCHAR(32) NOT NULL,
    operation_version BIGINT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    last_changed_at TIMESTAMPTZ NOT NULL,
    lock_version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE operation_event (
    id BIGSERIAL PRIMARY KEY,
    operation_id UUID NOT NULL REFERENCES operation_projection(id) ON DELETE CASCADE,
    sequence BIGINT NOT NULL,
    previous_status VARCHAR(32),
    new_status VARCHAR(32) NOT NULL,
    occurred_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT uq_operation_event_sequence UNIQUE (operation_id, sequence)
);

CREATE INDEX ix_operation_event_timeline ON operation_event(operation_id, sequence);
