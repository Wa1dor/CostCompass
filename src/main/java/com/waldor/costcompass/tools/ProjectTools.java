package com.waldor.costcompass.tools;

import java.util.Map;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.waldor.costcompass.events.models.ProjectEventType;
import com.waldor.costcompass.models.ProjectModel;
import com.waldor.costcompass.services.ProjectService;

@Component 
public class ProjectTools {
  private final ProjectService projectService;
  private String action;

  public ProjectTools(ProjectService projectService) {
    this.projectService = projectService;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  @Tool(description = "Update the project with new data for the user")
  public ProjectModel updateProject(ProjectModel projectModel) {
    action = ProjectEventType.PROJECT_UPDATED.toString();
    return projectService.updateProject(projectModel);
  }

  @Tool(description = "List all projects from the system for the user")
  public Map<Long, ProjectModel> listProjects() {
    return projectService.getAllProjects();
  }

  @Tool(description = "Create a new project for the user")
  public ProjectModel createProject(ProjectModel projectModel) {
    action = ProjectEventType.PROJECT_CREATED.toString();
    return projectService.addProject(projectModel);
  }

  @Tool(description = "Delete a project for the user, ask the user if he really want to delete it first")
  public void deleteProject(ProjectModel projectModel) {
    projectService.deleteProjectById(projectModel.getId());
    action = ProjectEventType.PROJECT_DELETED.toString();
  }

}
