package com.rhenan.taskflow.domain.model;

import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.exception.BusinessRuleException;
import com.rhenan.taskflow.domain.valueObjects.Description;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import com.rhenan.taskflow.domain.valueObjects.Title;
import com.rhenan.taskflow.domain.valueObjects.UserId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Task {

    private final TaskId id;
    private  final UserId userId;
    private final Title title;
    private final Description description;
    private ActivityStatus status;
    private final Instant createdAt;
    private Instant completedAt;

    private final List<SubTask> subTask = new ArrayList<>();

    private Task(TaskId id,
                 UserId userId,
                 Title title,
                 Description description,
                 ActivityStatus status,
                 Instant createdAt) {
        this.id = Objects.requireNonNull(id);
        this.userId = Objects.requireNonNull(userId);
        this.title = Objects.requireNonNull(title);
        this.description = description;
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public static Task createTask(UserId userId, Title title, Description description) {
        return new Task(
                TaskId.newTask(),
                userId,
                title,
                description,
                ActivityStatus.PENDING,
                Instant.now()
        );
    }

    public void addSubTask(Title title, Description description) {
        if (this.status == ActivityStatus.COMPLETED) {
            throw new BusinessRuleException("Não é possível adicionar subtarefas a uma tarefa concluída");
        }
        
        var newSubTask = SubTask.newSubTask(this.id, title, description);
        subTask.add(newSubTask);
    }

    public void updateStatus(ActivityStatus newStatus) {
        if (!this.status.allowTransition(newStatus)) {
            throw new BusinessRuleException("Transição inválida de " + this.status + " para " + newStatus);
        }
        
        this.status = newStatus;
        
        if (newStatus == ActivityStatus.COMPLETED) {
            this.completedAt = Instant.now();
        }
    }

    public void finish() {
        updateStatus(ActivityStatus.COMPLETED);
    }

    public TaskId getId() {
        return id;
    }

    public UserId getUserId() {
        return userId;
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

    public List<SubTask> getSubTask() {
        return subTask;
    }
}
