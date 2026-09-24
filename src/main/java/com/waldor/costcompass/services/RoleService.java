package com.waldor.costcompass.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.waldor.costcompass.events.EventPublishers;
import com.waldor.costcompass.events.models.RoleEvent;
import com.waldor.costcompass.events.models.RoleEventType;
import com.waldor.costcompass.models.RoleModel;

@Service
public class RoleService {

  private final Map<Long, RoleModel> roleMap = new HashMap<>();
  private final EventPublishers eventPublishers;
  private long nextId = 1;

  public RoleService(EventPublishers eventPublishers) {
    this.eventPublishers = eventPublishers;
    testRolesInit();
  }

  private long generateNextId() {
    return nextId++;
  }

  private void testRolesInit() {
    addRole(new RoleModel(null, "UX Designer", "test description"));
    addRole(new RoleModel(null, "Backend developer", "test description1"));
    addRole(new RoleModel(null, "Frontend Developer", "test description2"));
    addRole(new RoleModel(null, "Project Manager", "test description3"));
  }

  public RoleModel addRole(RoleModel roleModel) {
    long id = generateNextId();
    RoleModel newRoleModel = new RoleModel(id, roleModel.name(), roleModel.description());
    roleMap.put(id, newRoleModel);
    eventPublishers.publish("role-events", new RoleEvent(RoleEventType.ROLE_CREATED, newRoleModel));
    return newRoleModel;
  }

  public RoleModel updateRole(RoleModel roleModel) {
    if (!roleMap.containsKey(roleModel.id())) {
      return null;
    }
    eventPublishers.publish("role-events", new RoleEvent(RoleEventType.ROLE_UPDATED, roleModel));
    roleMap.replace(roleModel.id(), roleModel);
    return roleModel;
  }

  public RoleModel deleteRoleById(Long id) {
    if (!roleMap.containsKey(id)) {
      return null;
    }
    eventPublishers.publish("role-events", new RoleEvent(RoleEventType.ROLE_DELETED, getRolesById(id)));
    return roleMap.remove(id);
  }

  public RoleModel getRolesByName(String name) {
    RoleModel role = getAllRoles()
        .values()
        .stream()
        .filter(roles -> roles.name().equals(name))
        .findFirst()
        .orElse(null);
    return role;
  }

  public RoleModel getRolesById(Long id) {
    return roleMap.get(id);
  }

  public Map<Long, RoleModel> getAllRoles() {
    return roleMap;
  }
}
