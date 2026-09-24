package com.waldor.costcompass.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.waldor.costcompass.models.ResourceModel;
import com.waldor.costcompass.services.ResourceService;

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
@RequestMapping("/api/costcompass/resources")
public class ResourceController {

  private final ResourceService resourceService;

  public ResourceController(ResourceService resourceService) {
    this.resourceService = resourceService;
  }

  @GetMapping("")
  public List<ResourceModel> getAllResources() {
    return new ArrayList<>(resourceService.getAllResources().values());
  }

  @PostMapping("")
  public ResponseEntity<ResourceModel> postResource(@RequestBody ResourceModel resourceModel) {
    ResourceModel add = resourceService.addResource(resourceModel);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(add.id())
        .toUri();

    return ResponseEntity.created(location).body(add);
  }

  @PutMapping("")
  public ResponseEntity<ResourceModel> putResource(@RequestBody ResourceModel resourceModel) {
    ResourceModel updated = resourceService.updateResource(resourceModel);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ResourceModel> deleteResourceById(@PathVariable Long id) {
    resourceService.deleteResourceById(id);
    return ResponseEntity.noContent().build();
  }
}
