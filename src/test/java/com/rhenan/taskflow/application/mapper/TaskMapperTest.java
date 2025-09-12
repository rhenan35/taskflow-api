package com.rhenan.taskflow.application.mapper;

import com.rhenan.taskflow.application.dto.response.SubTaskResponse;
import com.rhenan.taskflow.application.dto.response.TaskResponse;
import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.model.SubTask;
import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import com.rhenan.taskflow.domain.valueObjects.UserId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    @Test
    void shouldMapTaskToResponseWithoutSubTasks() {
        Task task = createSampleTask();
        
        TaskResponse response = TaskMapper.toResponse(task);
        
        assertNotNull(response);
        assertEquals(task.getId().value(), response.id());
        assertEquals(task.getTitle(), response.title());
        assertEquals(task.getDescription(), response.description());
        assertEquals(task.getStatus(), response.status());
        assertEquals(task.getCreatedAt(), response.createdAt());
        assertEquals(task.getCompletedAt(), response.completedAt());
        assertEquals(task.getUserId().value(), response.userId());
        assertTrue(response.subTasks().isEmpty());
    }

    @Test
    void shouldMapTaskToResponseWithSubTasks() {
        Task task = createSampleTaskWithSubTasks();
        
        TaskResponse response = TaskMapper.toResponse(task);
        
        assertNotNull(response);
        assertEquals(task.getId().value(), response.id());
        assertEquals(2, response.subTasks().size());
        
        SubTaskResponse firstSubTask = response.subTasks().get(0);
        assertEquals("SubTask 1", firstSubTask.title());
        assertEquals(ActivityStatus.PENDING, firstSubTask.status());
    }

    @Test
    void shouldMapTaskToResponseWithSpecificSubTasks() {
        Task task = createSampleTask();
        List<SubTask> customSubTasks = List.of(
            createSubTask("Custom SubTask", ActivityStatus.COMPLETED)
        );
        
        TaskResponse response = TaskMapper.toResponse(task, customSubTasks);
        
        assertNotNull(response);
        assertEquals(1, response.subTasks().size());
        assertEquals("Custom SubTask", response.subTasks().get(0).title());
        assertEquals(ActivityStatus.COMPLETED, response.subTasks().get(0).status());
    }

    @Test
    void shouldHandleNullSubTasksList() {
        Task task = createSampleTask();
        
        TaskResponse response = TaskMapper.toResponse(task, null);
        
        assertNotNull(response);
        assertTrue(response.subTasks().isEmpty());
    }

    private Task createSampleTask() {
        return Task.createTask(
            UserId.fromString("550e8400-e29b-41d4-a716-446655440000"),
            "Sample Task",
            "Sample Description"
        );
    }

    private Task createSampleTaskWithSubTasks() {
        Task task = createSampleTask();
        task.addSubTask("SubTask 1", null);
        task.addSubTask("SubTask 2", "SubTask 2 Description");
        return task;
    }

    private SubTask createSubTask(String title, ActivityStatus status) {
        SubTask subTask = SubTask.newSubTask(
            TaskId.fromString("550e8400-e29b-41d4-a716-446655440001"),
            title,
            null
        );
        if (status != ActivityStatus.PENDING) {
            subTask.updateStatus(status);
        }
        return subTask;
    }
}