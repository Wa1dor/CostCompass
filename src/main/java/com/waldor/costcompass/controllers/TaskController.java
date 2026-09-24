package com.waldor.costcompass.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.waldor.costcompass.models.TaskModel;
import com.waldor.costcompass.services.TaskService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/costcompass/tasks")
public class TaskController {

  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping("")
  public List<TaskModel> getAllTasks() {
    return new ArrayList<>(taskService.getAllTasks().values());
  }

  @GetMapping("/{id}")
  public TaskModel getTasksById(@PathVariable Long id) {
    return taskService.getTaskById(id);
  }

  @PostMapping("")
  public ResponseEntity<TaskModel> postTask(@RequestBody TaskModel taskModel) {
    TaskModel add = taskService.addTask(taskModel);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(add.id())
        .toUri();

    return ResponseEntity.created(location).body(add);
  }

  @PutMapping("")
  public ResponseEntity<TaskModel> putTask(@RequestBody TaskModel taskModel) {
    TaskModel updated = taskService.updateTask(taskModel);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<TaskModel> deleteTaskById(@PathVariable Long id) {
    taskService.deleteTaskById(id);
    return ResponseEntity.noContent().build();
  }
  

}
