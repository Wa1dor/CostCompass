package com.waldor.costcompass.tools;

import java.util.Map;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.waldor.costcompass.events.models.RoleEventType;
import com.waldor.costcompass.models.RoleModel;
import com.waldor.costcompass.services.RoleService;

@Component 
public class RoleTools {
  private final RoleService roleService;
  private String action;

  public RoleTools(RoleService roleService) {
    this.roleService = roleService;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  @Tool(description = "Update the role with new data for the user")
  public RoleModel updateRole(RoleModel roleModel) {
      action = RoleEventType.ROLE_UPDATED.toString();
      return roleService.updateRole(roleModel);
  }

  @Tool(description = "List all roles from the system for the user")
  public Map<Long, RoleModel> listRoles() {
    return roleService.getAllRoles();
  }

  @Tool(description = "Create a new role for the user")
  public RoleModel createRole(RoleModel roleModel) {
    action = RoleEventType.ROLE_CREATED.toString();
    return roleService.addRole(roleModel);
  }

  @Tool(description = "Delete a role for the user, ask the user if he really want to delete it first")
  public void deleteRole(RoleModel roleModel) {
    roleService.deleteRoleById(roleModel.id());
    action = RoleEventType.ROLE_DELETED.toString();
  }
}
