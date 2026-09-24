package com.waldor.costcompass.tools;

import java.util.Map;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.waldor.costcompass.events.models.TaskEventType;
import com.waldor.costcompass.models.TaskModel;
import com.waldor.costcompass.services.TaskService;

@Component 
public class TaskTools {
  private final TaskService taskService;
  private String action;

  public TaskTools(TaskService taskService) {
    this.taskService = taskService;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  @Tool(description = "Update the task with new data for the user")
  public TaskModel updateTask(TaskModel taskModel) {
      action = TaskEventType.TASK_UPDATED.toString();
      return taskService.updateTask(taskModel);
  }

  @Tool(description = "List all tasks from the system for the user")
  public Map<Long, TaskModel> listTasks() {
    return taskService.getAllTasks();
  }

  @Tool(description = "Create a new task for the user")
  public TaskModel createTask(TaskModel taskModel) {
    action = TaskEventType.TASK_CREATED.toString();
    return taskService.addTask(taskModel);
  }

  @Tool(description = "Delete a task for the user, ask the user if he really want to delete it first")
  public void deleteTask(TaskModel taskModel) {
    taskService.deleteTaskById(taskModel.id());
    action = TaskEventType.TASK_DELETED.toString();
  }
}
