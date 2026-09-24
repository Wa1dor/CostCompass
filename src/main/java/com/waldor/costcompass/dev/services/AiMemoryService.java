package com.waldor.costcompass.dev.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.waldor.costcompass.dev.models.AiMemoryCategory;
import com.waldor.costcompass.dev.models.AiMemoryModel;
import com.waldor.costcompass.db.repository.AiMemoryRepository;

@Service
public class AiMemoryService {

	private AiMemoryRepository aiMemoryRepository;

	public AiMemoryService(AiMemoryRepository aiMemoryRepository) {
		this.aiMemoryRepository = aiMemoryRepository;
	}

	public AiMemoryModel saveMemory(String content, AiMemoryCategory category, String createdBy,
			Instant updatedAt) {
		AiMemoryModel aiMemoryModel = new AiMemoryModel(content, category, createdBy, updatedAt);
		return aiMemoryRepository.save(aiMemoryModel);
	}

	public List<AiMemoryModel> getAllMemories() {
		return aiMemoryRepository.findAll();
	}

	public Optional<AiMemoryModel> getMemoryById(Long id) {
		return aiMemoryRepository.findById(id);
	}

	public AiMemoryModel deleteMemoryById(Long id) {
		AiMemoryModel memory = getMemoryById(id)
				.orElseThrow(() -> new RuntimeException());
		aiMemoryRepository.deleteById(memory.getId());
		return memory;
	}

}
