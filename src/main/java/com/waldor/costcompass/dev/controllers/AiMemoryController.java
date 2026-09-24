package com.waldor.costcompass.dev.controllers;

import java.net.URI;
import java.util.List;

import com.waldor.costcompass.dev.dto.AiMemoryDto;
import com.waldor.costcompass.dev.models.AiMemoryModel;
import com.waldor.costcompass.dev.services.AiMemoryService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/costcompass/memory")
public class AiMemoryController {

  private AiMemoryService aiMemoryService;

  public AiMemoryController(AiMemoryService aiMemoryService) {
    this.aiMemoryService = aiMemoryService;
  }

  @PostMapping("")
  public ResponseEntity<AiMemoryModel> saveMemory(@RequestBody AiMemoryDto aiMemoryDto) {
    AiMemoryModel add = aiMemoryService.saveMemory(
        aiMemoryDto.content(), aiMemoryDto.category(),
        aiMemoryDto.createdBy(), aiMemoryDto.updatedAt());
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(add.getId())
        .toUri();
    return ResponseEntity.created(location).body(add);
  }

  @GetMapping("")
  public List<AiMemoryModel> getMemory() {
    return aiMemoryService.getAllMemories();
  }

  @GetMapping("/{id}")
  public ResponseEntity<AiMemoryModel> getMemoryById(@PathVariable Long id) {
    aiMemoryService.getMemoryById(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<AiMemoryModel> deleteMemoryById(@PathVariable Long id) {
    aiMemoryService.deleteMemoryById(id);
    return ResponseEntity.noContent().build();
  }

}
