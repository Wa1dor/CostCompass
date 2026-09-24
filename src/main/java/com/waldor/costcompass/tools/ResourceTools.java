package com.waldor.costcompass.tools;

import java.util.Map;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.waldor.costcompass.events.models.ResourceEventType;
import com.waldor.costcompass.models.ResourceModel;
import com.waldor.costcompass.services.ResourceService;

@Component 
public class ResourceTools {
  private final ResourceService resourceService;
  private String action;

  public ResourceTools(ResourceService resourceService) {
    this.resourceService = resourceService;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  @Tool(description = "Update the resource with new data for the user")
  public ResourceModel updateResource(ResourceModel resourceModel) {
      action = ResourceEventType.RESOURCE_UPDATED.toString();
      return resourceService.updateResource(resourceModel);
  }

  @Tool(description = "List all resources from the system for the user")
  public Map<Long, ResourceModel> listResources() {
    return resourceService.getAllResources();
  }

  @Tool(description = "Create a new resource for the user")
  public ResourceModel createResource(ResourceModel resourceModel) {
    action = ResourceEventType.RESOURCE_CREATED.toString();
    return resourceService.addResource(resourceModel);
  }

  @Tool(description = "Delete a resource for the user, ask the user if he really want to delete it first")
  public void deleteResource(ResourceModel resourceModel) {
    resourceService.deleteResourceById(resourceModel.id());
    action = ResourceEventType.RESOURCE_DELETED.toString();
  }
}
