package com.waldor.costcompass.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.waldor.costcompass.dev.models.AiMemoryModel;

public interface AiMemoryRepository extends JpaRepository<AiMemoryModel, Long> {
    List<AiMemoryModel> findByResolvedFalse();
}