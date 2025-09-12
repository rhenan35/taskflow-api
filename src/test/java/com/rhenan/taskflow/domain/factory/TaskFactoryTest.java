package com.rhenan.taskflow.domain.factory;

import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.valueObjects.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskFactoryTest {
    
    @Test
    void deveCriarTaskComTituloEDescricao() {
        UserId userId = UserId.newUser();
        String title = "Minha Task";
        String description = "Descrição da task";
        
        Task task = TaskFactory.createTask(userId, title, description);
        
        assertNotNull(task);
        assertNotNull(task.getId());
        assertEquals(userId, task.getUserId());
        assertEquals(title, task.getTitle().value());
        assertEquals(description, task.getDescription().value());
        assertEquals(ActivityStatus.PENDING, task.getStatus());
        assertNotNull(task.getCreatedAt());
        assertNull(task.getCompletedAt());
        assertTrue(task.getSubTask().isEmpty());
    }
    
    @Test
    void deveCriarTaskComTituloSemDescricao() {
        UserId userId = UserId.newUser();
        String title = "Task sem descrição";
        
        Task task = TaskFactory.createTask(userId, title, null);
        
        assertNotNull(task);
        assertEquals(title, task.getTitle().value());
        assertNull(task.getDescription());
        assertEquals(ActivityStatus.PENDING, task.getStatus());
    }
    
    @Test
    void naoDevePermitirUserIdNulo() {
        assertThrows(NullPointerException.class, () -> 
            TaskFactory.createTask(null, "Título", "Descrição")
        );
    }
    
    @Test
    void naoDevePermitirTituloNulo() {
        UserId userId = UserId.newUser();
        
        assertThrows(NullPointerException.class, () -> 
            TaskFactory.createTask(userId, null, "Descrição")
        );
    }
    
    @Test
    void naoDevePermitirTituloVazio() {
        UserId userId = UserId.newUser();
        
        assertThrows(IllegalArgumentException.class, () -> 
            TaskFactory.createTask(userId, "", "Descrição")
        );
    }
    
    @Test
    void naoDevePermitirTituloApenasEspacos() {
        UserId userId = UserId.newUser();
        
        assertThrows(IllegalArgumentException.class, () -> 
            TaskFactory.createTask(userId, "   ", "Descrição")
        );
    }
}