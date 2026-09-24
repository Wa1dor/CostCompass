package com.waldor.costcompass.events;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.waldor.costcompass.events.models.ProjectEvent;
import com.waldor.costcompass.events.models.ResourceEvent;
import com.waldor.costcompass.events.models.RoleEvent;
import com.waldor.costcompass.events.models.TaskEvent;
import com.waldor.costcompass.services.AuditLogService;

@Component
public class EventListeners {

  private final AuditLogService auditLogService;

  public EventListeners(AuditLogService auditLogService) {
    this.auditLogService = auditLogService;
  }

  @KafkaListener(topics = "task-events", groupId = "costcompass")
  public void tasksOnMessage(TaskEvent event) {
    System.out.println("Mottaget: " + event);
    auditLogService.record("task-events", event);
  }

  @KafkaListener(topics = "role-events", groupId = "costcompass")
  public void rolesOnMessage(RoleEvent event) {
    System.out.println("Mottaget: " + event);
    auditLogService.record("role-events", event);
  }

  @KafkaListener(topics = "resource-events", groupId = "costcompass")
  public void resourcesOnMessage(ResourceEvent event) {
    System.out.println("Mottaget: " + event);
    auditLogService.record("resource-events", event);
  }

  @KafkaListener(topics = "project-events", groupId = "costcompass")
  public void projectsOnMessage(ProjectEvent event) {
    System.out.println("Mottaget: " + event);
    auditLogService.record("project-events", event);
  }
}
