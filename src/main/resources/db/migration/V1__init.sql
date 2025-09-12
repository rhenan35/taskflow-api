CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE t_user (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name            VARCHAR(150) NOT NULL,
    email           VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE t_task (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title           VARCHAR(200) NOT NULL,
    description     TEXT,
    status          VARCHAR(30) NOT NULL CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED')),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    completed_at    TIMESTAMPTZ,
    user_id         UUID NOT NULL REFERENCES t_user(id) ON DELETE CASCADE
);

CREATE TABLE t_subtask (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title           VARCHAR(200) NOT NULL,
    description     TEXT,
    status          VARCHAR(30) NOT NULL CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED')),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    completed_at    TIMESTAMPTZ,
    task_id         UUID NOT NULL REFERENCES t_task(id) ON DELETE CASCADE
);

CREATE INDEX idx_user_email ON t_user(email);
CREATE INDEX idx_task_user_id ON t_task(user_id);
CREATE INDEX idx_task_status ON t_task(status);
CREATE INDEX idx_task_created_at ON t_task(created_at DESC);
CREATE INDEX idx_task_user_status ON t_task(user_id, status);
CREATE INDEX idx_subtask_task_id ON t_subtask(task_id);
CREATE INDEX idx_subtask_status ON t_subtask(status);
CREATE INDEX idx_subtask_task_status ON t_subtask(task_id, status);