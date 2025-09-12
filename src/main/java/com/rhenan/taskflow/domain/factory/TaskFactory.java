package com.rhenan.taskflow.domain.factory;

import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.valueObjects.Description;
import com.rhenan.taskflow.domain.valueObjects.Title;
import com.rhenan.taskflow.domain.valueObjects.UserId;

public class TaskFactory {
    
    private TaskFactory() {}
    
    public static Task createTask(UserId userId, String title, String description) {
        Title taskTitle = new Title(title);
        Description taskDescription = description != null ? new Description(description) : null;
        
        return Task.createTask(userId, taskTitle, taskDescription);
    }
}