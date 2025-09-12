package com.rhenan.taskflow.domain.model;

import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.exception.BusinessRuleException;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import com.rhenan.taskflow.domain.valueObjects.UserId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Task {

    private final TaskId id;
    private  final UserId userId;
    private String title;
    private String description;
    private ActivityStatus status;
    private final Instant createdAt;
    private Instant completedAt;

    private final List<SubTask> subTask = new ArrayList<>();

    private Task(TaskId id,
                 UserId userId,
                 String title,
                 String description,
                 ActivityStatus status,
                 Instant createdAt) {
        this.id = Objects.requireNonNull(id);
        this.userId = Objects.requireNonNull(userId);
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

    public static Task createTask(UserId userId, String title, String description) {
        return new Task(
                TaskId.newTask(),
                userId,
                title,
                description,
                ActivityStatus.PENDING,
                Instant.now()
        );
    }

    public void addSubTask(String title, String description) {
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
        
        if (newStatus == ActivityStatus.COMPLETED && hasIncompletedSubTasks()) {
            throw new BusinessRuleException("Não é possível concluir a tarefa enquanto houver subtarefas pendentes");
        }
        
        this.status = newStatus;
        
        if (newStatus == ActivityStatus.COMPLETED) {
            this.completedAt = Instant.now();
        }
    }
    
    private boolean hasIncompletedSubTasks() {
        return subTask.stream().anyMatch(st -> st.getStatus() != ActivityStatus.COMPLETED);
    }

    public void finish() {
        updateStatus(ActivityStatus.COMPLETED);
    }
    
    public void updateTitle(String newTitle) {
        if (this.status == ActivityStatus.COMPLETED) {
            throw new BusinessRuleException("Não é possível atualizar uma tarefa concluída");
        }
        this.title = validateTitle(newTitle);
    }
    
    public void updateDescription(String newDescription) {
        if (this.status == ActivityStatus.COMPLETED) {
            throw new BusinessRuleException("Não é possível atualizar uma tarefa concluída");
        }
        this.description = validateDescription(newDescription);
    }

    public TaskId getId() {
        return id;
    }

    public UserId getUserId() {
        return userId;
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

    public List<SubTask> getSubTask() {
        return subTask;
    }
}
