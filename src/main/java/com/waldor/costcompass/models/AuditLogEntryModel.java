package com.waldor.costcompass.models;

import java.time.Instant;

public record AuditLogEntryModel(long id, String topic, Instant timestamp, Object event) {

}
