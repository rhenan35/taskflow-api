package com.rhenan.taskflow.application.usecase.subtask;

import com.rhenan.taskflow.application.dto.request.PageRequest;
import com.rhenan.taskflow.application.dto.request.SubTaskFilterRequest;
import com.rhenan.taskflow.application.dto.response.PageResponse;
import com.rhenan.taskflow.application.dto.response.SubTaskResponse;
import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.model.SubTask;
import com.rhenan.taskflow.domain.repository.SubTaskRepository;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindSubTasksWithFiltersUseCaseTest {

    @Mock
    private SubTaskRepository subTaskRepository;

    @InjectMocks
    private FindSubTasksWithFiltersUseCase findSubTasksWithFiltersUseCase;

    private SubTaskFilterRequest filters;
    private PageRequest pageRequest;
    private SubTask subTask;
    private PageResponse<SubTask> subTaskPage;

    @BeforeEach
    void setUp() {
        UUID taskUuid = UUID.randomUUID();
        
        filters = SubTaskFilterRequest.builder()
            .status(ActivityStatus.PENDING)
            .taskId(taskUuid)
            .title("Test")
            .build();
        
        pageRequest = PageRequest.of(0, 10, "createdAt", "desc");
        
        subTask = SubTask.newSubTask(
            new TaskId(taskUuid),
            "Test SubTask",
            "Test Description"
        );
        
        subTaskPage = PageResponse.of(
            List.of(subTask),
            0,
            10,
            1L
        );
    }

    @Test
    void shouldExecuteSuccessfully() {
        when(subTaskRepository.findWithFilters(filters, pageRequest)).thenReturn(subTaskPage);

        PageResponse<SubTaskResponse> result = findSubTasksWithFiltersUseCase.execute(filters, pageRequest);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals(0, result.page());
        assertEquals(10, result.size());
        assertEquals(1L, result.totalElements());
        
        SubTaskResponse subTaskResponse = result.content().get(0);
        assertEquals("Test SubTask", subTaskResponse.title());
        assertEquals("Test Description", subTaskResponse.description());
        assertEquals(ActivityStatus.PENDING, subTaskResponse.status());
        
        verify(subTaskRepository).findWithFilters(filters, pageRequest);
    }

    @Test
    void shouldReturnEmptyPageWhenNoSubTasksFound() {
        PageResponse<SubTask> emptyPage = PageResponse.of(List.of(), 0, 10, 0L);
        when(subTaskRepository.findWithFilters(filters, pageRequest)).thenReturn(emptyPage);

        PageResponse<SubTaskResponse> result = findSubTasksWithFiltersUseCase.execute(filters, pageRequest);

        assertNotNull(result);
        assertTrue(result.content().isEmpty());
        assertEquals(0, result.page());
        assertEquals(10, result.size());
        assertEquals(0L, result.totalElements());
        
        verify(subTaskRepository).findWithFilters(filters, pageRequest);
    }

    @Test
    void shouldHandleMultipleSubTasks() {
        UUID taskUuid = UUID.randomUUID();
        SubTask subTask1 = SubTask.newSubTask(new TaskId(taskUuid), "SubTask 1", "Description 1");
        SubTask subTask2 = SubTask.newSubTask(new TaskId(taskUuid), "SubTask 2", "Description 2");
        
        PageResponse<SubTask> multipleSubTasksPage = PageResponse.of(
            List.of(subTask1, subTask2),
            0,
            10,
            2L
        );
        
        when(subTaskRepository.findWithFilters(filters, pageRequest)).thenReturn(multipleSubTasksPage);

        PageResponse<SubTaskResponse> result = findSubTasksWithFiltersUseCase.execute(filters, pageRequest);

        assertNotNull(result);
        assertEquals(2, result.content().size());
        assertEquals(2L, result.totalElements());
        
        verify(subTaskRepository).findWithFilters(filters, pageRequest);
    }
}