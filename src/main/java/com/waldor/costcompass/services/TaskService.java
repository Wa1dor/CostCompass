package com.waldor.costcompass.services;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.waldor.costcompass.events.EventPublishers;
import com.waldor.costcompass.events.models.TaskEvent;
import com.waldor.costcompass.events.models.TaskEventType;
import com.waldor.costcompass.models.ResourceModel;
import com.waldor.costcompass.models.TaskModel;

@Service
public class TaskService {
  private final ResourceService resourceService;
  private final EventPublishers eventPublishers;
  private Map<Long, TaskModel> taskMap = new HashMap<>();

  private long nextId = 1;

  public TaskService(ResourceService resourceService, EventPublishers eventPublishers) {
    this.resourceService = resourceService;
    this.eventPublishers = eventPublishers;
    createTaskInit();
  }

  private long generateNextId() {
    return nextId++;
  }

  private void createTaskInit() {
    ResourceModel oneResource = resourceService.getAllResources().values()
        .stream()
        .filter(resource -> resource.id().equals(1L))
        .findFirst()
        .orElse(null);
        
    ResourceModel tworesource = resourceService.getAllResources().values()
        .stream()
        .filter(resource -> resource.id().equals(2L))
        .findFirst()
        .orElse(null);
        
    ResourceModel threeResource = resourceService.getAllResources().values()
        .stream()
        .filter(resource -> resource.id().equals(3L))
        .findFirst()
        .orElse(null);
    addTask(new TaskModel(null, "Backend Development", "Test Descirption task", oneResource, Duration.ofMinutes(30L),
        Duration.ofMinutes(10L)));
    addTask(new TaskModel(null, "Frontend Development", "Test Descirption task", tworesource, Duration.ofMinutes(30L),
        Duration.ofMinutes(10L)));
    addTask(new TaskModel(null, "Devops Development", "Test Descirption task", threeResource, Duration.ofMinutes(30L),
        Duration.ofMinutes(10L)));
    addTask(new TaskModel(null, "UX Development", "Test Descirption task", oneResource, Duration.ofMinutes(30L),
        Duration.ofMinutes(10L)));
  }

  public TaskModel addTask(TaskModel taskModel) {
    long id = generateNextId();
    TaskModel newTaskModel = new TaskModel(id, taskModel.name(), taskModel.description(), taskModel.resource(),
        taskModel.estimatedExecutionTime(), taskModel.estimatedVerificationTime());
    taskMap.put(id, newTaskModel);
    eventPublishers.publish("task-events", new TaskEvent(TaskEventType.TASK_CREATED, newTaskModel));
    return newTaskModel;
  }

  public TaskModel deleteTaskById(Long id) {
    if (!taskMap.containsKey(id)) {
      return null;
    }
    eventPublishers.publish("task-events", new TaskEvent(TaskEventType.TASK_DELETED, getTaskById(id)));
    return taskMap.remove(id);
  }

  public TaskModel updateTask(TaskModel taskModel) {
    if (!taskMap.containsKey(taskModel.id())) {
      return null;
    }
    eventPublishers.publish("task-events", new TaskEvent(TaskEventType.TASK_UPDATED, taskModel));
    taskMap.replace(taskModel.id(), taskModel);
    return taskModel;
  }

  public TaskModel getTaskByName(String name) {
    TaskModel task = getAllTasks().values().stream()
        .filter(t -> t.name().equals(name))
        .findFirst()
        .orElse(null);
    return task;
  }

  public TaskModel getTaskById(Long id) {
    Map<Long, TaskModel> allTasks = getAllTasks();
    return allTasks.get(id);
  }

  public Map<Long, TaskModel> getAllTasks() {
    return taskMap;
  }

}
