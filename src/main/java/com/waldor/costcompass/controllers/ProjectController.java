package com.waldor.costcompass.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.waldor.costcompass.models.ProjectModel;
import com.waldor.costcompass.services.ProjectService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/costcompass/projects")
public class ProjectController {

  private final ProjectService projectService;

  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  @GetMapping("")
  public List<ProjectModel> getAllProjects() {
    return new ArrayList<>(projectService.getAllProjects().values());
  }

  @PostMapping("")
  public ResponseEntity<ProjectModel> postProject(@RequestBody ProjectModel projectModel) {
    ProjectModel add = projectService.addProject(projectModel);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(add.getId())
        .toUri();

    return ResponseEntity.created(location).body(add);
  }

  @PutMapping("")
  public ResponseEntity<ProjectModel> putResource(@RequestBody ProjectModel projectModel) {
    ProjectModel updated = projectService.updateProject(projectModel);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ProjectModel> deleteResourceById(@PathVariable Long id) {
    projectService.deleteProjectById(id);
    return ResponseEntity.noContent().build();
  }
}
