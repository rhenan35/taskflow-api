CREATE TABLE t_user (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(150) NOT NULL,
    email           VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE t_task (
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(200) NOT NULL,
    description     TEXT,
    status          VARCHAR(30) NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    completed_at    TIMESTAMP,
    user_id         BIGINT NOT NULL REFERENCES t_user(id) ON DELETE CASCADE
);

CREATE TABLE t_subtask (
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(200) NOT NULL,
    description     TEXT,
    status          VARCHAR(30) NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    completed_at    TIMESTAMP,
    task_id         BIGINT NOT NULL REFERENCES t_task(id) ON DELETE CASCADE
);

CREATE INDEX idx_user_task ON t_task(user_id);
CREATE INDEX idx_task_status  ON t_task(status);
CREATE INDEX idx_subtask_status ON t_subtask(status);