package com.waldor.costcompass.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.waldor.costcompass.events.EventPublishers;
import com.waldor.costcompass.events.models.ResourceEvent;
import com.waldor.costcompass.events.models.ResourceEventType;
import com.waldor.costcompass.models.ResourceModel;
import com.waldor.costcompass.models.RoleModel;

@Service
public class ResourceService {
  private final RoleService roleService;
  private final EventPublishers eventPublishers;
  private final Logger logger = LoggerFactory.getLogger(ResourceService.class);

  private Map<Long, ResourceModel> resourceMap = new HashMap<>();

  private long nextId = 1;

  public ResourceService(RoleService roleService, EventPublishers eventPublishers) {
    this.roleService = roleService;
    this.eventPublishers = eventPublishers;
    createResourceInit();
  }

  private long generateNextId() {
    return nextId++;
  }

  private void createResourceInit() {
    List<RoleModel> rolesList1 = new ArrayList<>();
    List<RoleModel> rolesList2 = new ArrayList<>();
    List<RoleModel> rolesList3 = new ArrayList<>();
    rolesList1.add(roleService.getRolesByName("UX Designer"));
    rolesList2.add(roleService.getRolesByName("Backend developer"));
    rolesList3.add(roleService.getRolesByName("Frontend Developer"));
    addResource(new ResourceModel(null, rolesList1, BigDecimal.valueOf(1200), ResourceModel.ResourceType.HUMAN));
    addResource(new ResourceModel(null, rolesList2, BigDecimal.valueOf(1200), ResourceModel.ResourceType.HUMAN));
    addResource(new ResourceModel(null, rolesList3, BigDecimal.valueOf(500), ResourceModel.ResourceType.AI_DIRECTED));
    logger.info(resourceMap.toString());
  }

  public ResourceModel addResource(ResourceModel resourceModel) {
    long id = generateNextId();
    ResourceModel newResourceModel = new ResourceModel(id, resourceModel.roles(), resourceModel.hourlyPrice(),
        resourceModel.type());
    resourceMap.put(id, newResourceModel);
    eventPublishers.publish("resource-events", new ResourceEvent(ResourceEventType.RESOURCE_CREATED, newResourceModel));
    return newResourceModel;
  }

  public ResourceModel deleteResourceById(Long id) {
    if (!resourceMap.containsKey(id)) {
      return null;
    }
    eventPublishers.publish("resource-events", new ResourceEvent(ResourceEventType.RESOURCE_DELETED, getResourceById(id)));
    return resourceMap.remove(id);
  }

  public ResourceModel updateResource(ResourceModel resourceModel) {
    if (!resourceMap.containsKey(resourceModel.id())) {
      return null;
    }
    eventPublishers.publish("resource-events", new ResourceEvent(ResourceEventType.RESOURCE_UPDATED, resourceModel));
    resourceMap.replace(resourceModel.id(), resourceModel);
    return  resourceModel;
  }

  public ResourceModel getResourceById(Long id) {
    Map<Long, ResourceModel> allResources = getAllResources();
    return allResources.get(id);
  }

  public Map<Long, ResourceModel> getAllResources() {
    return resourceMap;
  }
}
