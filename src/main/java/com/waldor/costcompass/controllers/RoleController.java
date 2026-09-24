package com.waldor.costcompass.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.waldor.costcompass.models.RoleModel;
import com.waldor.costcompass.models.TaskModel;
import com.waldor.costcompass.services.RoleService;

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
@RequestMapping("/api/costcompass/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("")
    public List<RoleModel> getAllRoles() {
        return new ArrayList<>(roleService.getAllRoles().values());
    }
    
    @GetMapping("/{id}")
    public RoleModel getRolesById(@PathVariable Long id) {
        return roleService.getRolesById(id);
    }

    @PostMapping("")
    public ResponseEntity<RoleModel> postRole(@RequestBody RoleModel roleModel) {
      RoleModel add = roleService.addRole(roleModel);
      URI location = ServletUriComponentsBuilder.fromCurrentRequest()
      .path("/{id}")
      .buildAndExpand(add.id())
      .toUri();
        
      return ResponseEntity.created(location).body(add);
    }

  @PutMapping("")
  public ResponseEntity<RoleModel> putTask(@RequestBody RoleModel roleModel) {
    RoleModel updated = roleService.updateRole(roleModel);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<RoleModel> deleteTaskById(@PathVariable Long id) {
    roleService.deleteRoleById(id);
    return ResponseEntity.noContent().build();
  }
}
