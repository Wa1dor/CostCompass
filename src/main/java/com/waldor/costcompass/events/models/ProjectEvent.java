package com.waldor.costcompass.events.models;

import com.waldor.costcompass.models.ProjectModel;

public record ProjectEvent(ProjectEventType projectEventType, ProjectModel projectModel) {
  
}
