package com.waldor.costcompass.dev.models;

import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dev_tasks")
public class DevTaskModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DevTaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DevTaskType type;

    private String assignedTo;
    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;

    public DevTaskModel() {
    }

    public DevTaskModel(String title, String description, DevTaskType type, String assignedTo, String createdBy) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.assignedTo = assignedTo;
        this.createdBy = createdBy;
        this.status = DevTaskStatus.TODO;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DevTaskStatus getStatus() {
        return status;
    }

    public void setStatus(DevTaskStatus status) {
        this.status = status;
    }

    public DevTaskType getType() {
        return type;
    }

    public void setType(DevTaskType type) {
        this.type = type;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

}