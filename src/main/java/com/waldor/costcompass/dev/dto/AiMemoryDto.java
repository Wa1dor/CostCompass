package com.waldor.costcompass.dev.dto;

import java.time.Instant;

import com.waldor.costcompass.dev.models.AiMemoryCategory;

public record AiMemoryDto(String content, AiMemoryCategory category, String createdBy, Instant createdAt, Instant updatedAt) {
}
