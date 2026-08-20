package com.waldor.costcompass.models;

import java.math.BigDecimal;
import java.util.List;

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
        return tasks;
    }
    public void setTasks(List<TaskModel> tasks) {
        this.tasks = tasks;
    }

    public BigDecimal totalPrice(){
        BigDecimal sumPrice = tasks.stream()
        .map(item -> item.price())
        .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        return sumPrice;
    }

    public TaskModel addTask(TaskModel task){
        tasks.add(task);
        return task;
    }

}
