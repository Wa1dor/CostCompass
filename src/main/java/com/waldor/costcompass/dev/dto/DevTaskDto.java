package com.waldor.costcompass.dev.dto;

import java.time.Instant;
import com.waldor.costcompass.dev.models.DevTaskStatus;
import com.waldor.costcompass.dev.models.DevTaskType;

public record DevTaskDto(
        Long id,
        String title,
        String description,
        DevTaskStatus status,
        DevTaskType type,
        String assignedTo,
        String createdBy,
        Instant createdAt,
        Instant updatedAt) {
}