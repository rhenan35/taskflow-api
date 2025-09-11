package com.rhenan.taskflow.domain.model;

import com.rhenan.taskflow.domain.enums.ActivityStatus;
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

    public void addSubTask (Title title) {
        if (status == ActivityStatus.COMPLETED) {
            throw new IllegalStateException("Não é possivel adicionar uma subtask em uma task finalizada");
        }
        subTask.add(SubTask.newSubTask(this.id, title, description));
    }

    public void finish() {
        if (subTask.stream().anyMatch(sub -> sub.getStatus() != ActivityStatus.COMPLETED)) {
            throw new IllegalStateException("Todas as subtasks precisam ser concluidas antes de encerrar a task");
        }
        this.status = ActivityStatus.COMPLETED;
        this.completedAt = Instant.now();
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
