package com.waldor.costcompass.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.waldor.costcompass.models.AuditLogEntryModel;

@Service 
public class AuditLogService {
  private final List<AuditLogEntryModel> auditLogEntries = new CopyOnWriteArrayList<>();
  private final AtomicLong nextId = new AtomicLong();

  public void record(String topic, Object event) {
    auditLogEntries.add(new AuditLogEntryModel(nextId.getAndIncrement(), topic, Instant.now(), event));
  }

  public List<AuditLogEntryModel> getAll() {
    List<AuditLogEntryModel> copy = new ArrayList<>(auditLogEntries);
    Collections.reverse(copy);
    return copy;
  }

}
