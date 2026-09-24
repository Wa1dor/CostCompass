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
@Table(name = "memories")
public class AiMemoryModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AiMemoryCategory category;

  private String createdBy;

  private Instant createdAt;

  private Instant updatedAt;

  private boolean resolved;

  public AiMemoryModel() {

  }

  public AiMemoryModel(String content, AiMemoryCategory category, String createdBy, Instant updatedAt) {
    this.content = content;
    this.category = category;
    this.createdBy = createdBy;
    this.createdAt = Instant.now();
    this.updatedAt = updatedAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public AiMemoryCategory getCategory() {
    return category;
  }

  public void setCategory(AiMemoryCategory category) {
    this.category = category;
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

  public boolean isResolved() {
    return resolved;
  }

  public void setResolved(boolean resolved) {
    this.resolved = resolved;
  }
}
