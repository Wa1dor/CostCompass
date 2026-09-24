package com.waldor.costcompass.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectModel {
  Long id;
  String customer;
  String projectName;
  List<TaskModel> tasks;

  public ProjectModel(Long id, String customer, String projectName, List<TaskModel> tasks) {
    this.id = id;
    this.customer = customer;
    this.projectName = projectName;
    this.tasks = tasks;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCustomer() {
    return customer;
  }

  public void setCustomer(String customer) {
    this.customer = customer;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public List<TaskModel> getTasks() {
    return new ArrayList<>(tasks);
  }

  public void setTasks(List<TaskModel> tasks) {
    this.tasks = new ArrayList<>(tasks);
  }

  @Override
  public String toString() {
    return "ProjectModel [id=" + id + ", customer=" + customer + ", projectName=" + projectName + ", tasks=" + tasks
        + "]";
  }

  public TaskModel addTask(TaskModel task) {
    tasks.add(task);
    return task;
  }

  @JsonProperty("price")
  public BigDecimal price() {
    return tasks.stream()
        .map(TaskModel::price)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
