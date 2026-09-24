package com.waldor.costcompass.db.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.waldor.costcompass.dev.models.DevTaskModel;
import com.waldor.costcompass.dev.models.DevTaskStatus;

public interface DevTaskRepository extends JpaRepository<DevTaskModel, Long> {
    List<DevTaskModel> findByStatus(DevTaskStatus status);

    List<DevTaskModel> findByStatusNot(DevTaskStatus status);
}