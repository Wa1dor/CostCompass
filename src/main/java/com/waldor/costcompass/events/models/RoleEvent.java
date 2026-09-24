package com.waldor.costcompass.events.models;

import com.waldor.costcompass.models.RoleModel;

public record RoleEvent(RoleEventType roleEventType, RoleModel roleModel) {
  
}
