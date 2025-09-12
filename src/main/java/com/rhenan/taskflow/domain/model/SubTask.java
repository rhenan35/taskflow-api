package com.rhenan.taskflow.domain.model;

import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.exception.BusinessRuleException;
import com.rhenan.taskflow.domain.valueObjects.SubTaskId;
import com.rhenan.taskflow.domain.valueObjects.TaskId;

import java.time.Instant;
import java.util.Objects;

public class SubTask {

    private final SubTaskId id;
    private final TaskId taskId;
    private final String title;
    private final String description;
    private ActivityStatus status;
    private final Instant createdAt;
    private Instant completedAt;

    private SubTask(SubTaskId id,
                    TaskId taskId,
                    String title,
                    String description,
                    ActivityStatus status,
                    Instant createdAt) {
        this.id = Objects.requireNonNull(id);
        this.taskId = Objects.requireNonNull(taskId);
        this.title = validateTitle(title);
        this.description = validateDescription(description);
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
    }
    
    private String validateTitle(String title) {
        Objects.requireNonNull(title, "Title não pode ser nulo!");
        String trimmedTitle = title.trim();
        if (trimmedTitle.isBlank()) {
            throw new IllegalArgumentException("Title não pode ser vazio!");
        }
        if (trimmedTitle.length() > 200) {
            throw new IllegalArgumentException("Title passou de 200 caracteres");
        }
        return trimmedTitle;
    }
    
    private String validateDescription(String description) {
        return description != null ? description.trim() : "";
    }

    public static SubTask newSubTask(TaskId taskId, String title, String description) {
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
        Objects.requireNonNull(newStatus, "Status não pode ser nulo");
        
        if (!this.status.allowTransition(newStatus)) {
            throw new BusinessRuleException(
                String.format("Transição inválida de %s para %s", this.status, newStatus)
            );
        }
        
        this.status = newStatus;
        if (newStatus == ActivityStatus.COMPLETED) {
            this.completedAt = Instant.now();
        }
    }

    public void finish() {
        updateStatus(ActivityStatus.COMPLETED);
    }

    public SubTaskId getId() {
        return id;
    }

    public TaskId getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
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
