package com.waldor.costcompass.dev.tools;

import java.util.List;
import java.util.Optional;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.waldor.costcompass.db.repository.AiMemoryRepository;
import com.waldor.costcompass.dev.models.AiMemoryCategory;
import com.waldor.costcompass.dev.models.AiMemoryModel;
import com.waldor.costcompass.dev.services.AiMemoryService;

@Component
public class DevMemoryTools {

    private AiMemoryService aiMemoryService;
    private AiMemoryRepository aiMemoryRepository;

    public DevMemoryTools(AiMemoryService aiMemoryService, AiMemoryRepository aiMemoryRepository) {
        this.aiMemoryService = aiMemoryService;
        this.aiMemoryRepository = aiMemoryRepository;
    }

    @Tool(description = "Save a development memory (a task, decision or note about the development of the project")
    public AiMemoryModel saveMemory(
            @ToolParam(description = "The content for the memory to save") String content,
            @ToolParam(description = "A category label") AiMemoryCategory category) {
        return aiMemoryService.saveMemory(content, category, "ai-chat", null);
    }

    @Tool(description = "List all memories")
    public List<AiMemoryModel> listMemories() {
        return aiMemoryService.getAllMemories();
    }

    @Tool(description = "Get one memory by id")
    public Optional<AiMemoryModel> getMemoryById(AiMemoryModel aiMemoryModel) {
        return aiMemoryService.getMemoryById(aiMemoryModel.getId());
    }

    @Tool(description = "Markera ett minne som löst, t.ex. när en bugg det beskriver är åtgärdad.")
    public AiMemoryModel markMemoryResolved(
            @ToolParam(description = "Id på minnet som ska markeras som löst") Long id) {
        AiMemoryModel memory = aiMemoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inget minne med id " + id));
        memory.setResolved(true);
        return aiMemoryRepository.save(memory);
    }
}
