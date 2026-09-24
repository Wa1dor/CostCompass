package com.waldor.costcompass.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.waldor.costcompass.events.EventPublishers;
import com.waldor.costcompass.events.models.ProjectEvent;
import com.waldor.costcompass.events.models.ProjectEventType;
import com.waldor.costcompass.models.ProjectModel;
import com.waldor.costcompass.models.TaskModel;

@Service
public class ProjectService {
  private final TaskService taskService;
  private final EventPublishers eventPublishers;
  private Map<Long, ProjectModel> projectMap = new HashMap<>();

  private long nextId = 1;

  public ProjectService(TaskService taskService, EventPublishers eventPublishers) {
    this.taskService = taskService;
    this.eventPublishers = eventPublishers;
    createProjectInit();
  }

  private long generateNextId() {
    return nextId++;
  }

  private void createProjectInit() {
    List<TaskModel> taskList = new ArrayList<>();
    taskList.add(getTaskById(1L));
    ProjectModel project = addProject(new ProjectModel(null, "ACME CORP", "Plattformsmigrering", taskList));
    ProjectModel project1 = addProject(new ProjectModel(null, "NORDIC RETAIL AB", "E-handel omdesign", taskList));
    project.addTask(getTaskById(2L));
    project1.addTask(getTaskById(2L));

  }

  public ProjectModel addProject(ProjectModel projectModel) {
    long id = generateNextId();
    ProjectModel newProject = new ProjectModel(id, projectModel.getCustomer(), projectModel.getProjectName(),
        projectModel.getTasks());
    projectMap.put(id, newProject);
    eventPublishers.publish("project-events", new ProjectEvent(ProjectEventType.PROJECT_CREATED, getProjectById(id)));
    return newProject;
  }

  public ProjectModel deleteProjectById(Long id) {
    if (!projectMap.containsKey(id)) {
      return null;
    }
    eventPublishers.publish("project-events", new ProjectEvent(ProjectEventType.PROJECT_DELETED, getProjectById(id)));
    return projectMap.remove(id);
  }

  public ProjectModel updateProject(ProjectModel projectModel) {
    if (!projectMap.containsKey(projectModel.getId())) {
      return null;
    }
    eventPublishers.publish("project-events", new ProjectEvent(ProjectEventType.PROJECT_UPDATED, getProjectById(projectModel.getId())));
    projectMap.replace(projectModel.getId(), projectModel);
    return projectModel;
  }

  public Map<Long, ProjectModel> getAllProjects() {
    return projectMap;
  }

  public ProjectModel getProjectById(Long id) {
    Map<Long, ProjectModel> allProjects = getAllProjects();
    return allProjects.get(id);
  }

  public TaskModel getTaskById(Long id) {
    return taskService.getTaskById(id);
  }

}
