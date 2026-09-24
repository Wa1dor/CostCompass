package com.waldor.costcompass.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.waldor.costcompass.models.AuditLogEntryModel;
import com.waldor.costcompass.services.AuditLogService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

@RestController 
@RequestMapping("/api/costcompass/auditlog")
public class AuditLogController {
  
  AuditLogService auditLogService;

  public AuditLogController(AuditLogService auditLogService) {
    this.auditLogService = auditLogService;
  }

  @GetMapping()
  public List<AuditLogEntryModel> getAll() {
      return auditLogService.getAll();
  }

}
