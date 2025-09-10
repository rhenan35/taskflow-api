package com.rhenan.taskflow.domain.model;

import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.valueObjects.Description;
import com.rhenan.taskflow.domain.valueObjects.SubTaskId;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import com.rhenan.taskflow.domain.valueObjects.Title;

import java.time.Instant;
import java.util.Objects;

public class SubTask {

    private final SubTaskId id;
    private final TaskId taskId;
    private final Title title;
    private final Description description;
    private ActivityStatus status;
    private final Instant createdAt;
    private Instant completedAt;

    private SubTask(SubTaskId id,
                    TaskId taskId,
                    Title title,
                    Description description,
                    ActivityStatus status,
                    Instant createdAt) {
        this.id = Objects.requireNonNull(id);
        this.taskId = Objects.requireNonNull(taskId);
        this.title = Objects.requireNonNull(title);
        this.description = description;
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public static SubTask newSubTask(TaskId taskId, Title title, Description description) {
        return new SubTask(
                SubTaskId.newSubTask(),
                taskId,
                title,
                description,
                ActivityStatus.PENDING,
                Instant.now()
        );
    }

    public void updateStatus(ActivityStatus newStatus) {
        if (!status.allowTransition(newStatus)) {
            throw new IllegalStateException("O stats " + status + " não pode ser alterado para " + newStatus);
        }
        this.status = newStatus;
        if (newStatus == ActivityStatus.COMPLETED) {
            this.completedAt = Instant.now();
        }
    }

    public SubTaskId getId() {
        return id;
    }

    public TaskId getTaskId() {
        return taskId;
    }

    public Title getTitle() {
        return title;
    }

    public Description getDescription() {
        return description;
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }
}
