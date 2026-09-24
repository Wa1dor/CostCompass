package com.waldor.costcompass.dev.tools;

import java.time.Instant;
import java.util.List;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.waldor.costcompass.db.repository.DevTaskRepository;
import com.waldor.costcompass.dev.models.DevTaskModel;
import com.waldor.costcompass.dev.models.DevTaskStatus;
import com.waldor.costcompass.dev.models.DevTaskType;

@Component
public class DevTaskTools {

    private final DevTaskRepository devTaskRepository;

    public DevTaskTools(DevTaskRepository devTaskRepository) {
        this.devTaskRepository = devTaskRepository;
    }

    @Tool(description = "Create a new development task to be tracked, e.g. a bug or feature.")
    public DevTaskModel createTask(
            @ToolParam(description = "Short title of the task") String title,
            @ToolParam(description = "More detailed description", required = false) String description,
            @ToolParam(description = "Type: BUG, FEATURE, REFACTOR, CHORE or DOCS") DevTaskType type,
            @ToolParam(description = "Who the task is assigned to, e.g. a name. Can be left empty.", required = false) String assignedTo,
            @ToolParam(description = "Who/what created the task, e.g. a name or 'AI'") String createdBy) {
        DevTaskModel task = new DevTaskModel(title, description, type, assignedTo, createdBy);
        return devTaskRepository.save(task);
    }

    @Tool(description = "Get all development tasks, optionally filtered by status. By default, completed (DONE) tasks are excluded to keep the list focused on active work - explicitly ask for status DONE to see completed tasks.")
    public List<DevTaskModel> listTasks(
            @ToolParam(description = "Filter by status: TODO, IN_PROGRESS, IN_REVIEW, DONE or BLOCKED. Leave empty to see all active (non-DONE) tasks.", required = false) DevTaskStatus status) {
        if (status != null) {
            return devTaskRepository.findByStatus(status);
        }
        return devTaskRepository.findByStatusNot(DevTaskStatus.DONE);
    }

    @Tool(description = "Update the status of an existing task.")
    public DevTaskModel updateTaskStatus(
            @ToolParam(description = "Id of the task") Long id,
            @ToolParam(description = "New status: TODO, IN_PROGRESS, IN_REVIEW, DONE or BLOCKED") DevTaskStatus status) {
        DevTaskModel task = devTaskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No task with id " + id));
        task.setStatus(status);
        task.setUpdatedAt(Instant.now());
        return devTaskRepository.save(task);
    }

    @Tool(description = "Assign a task to a person.")
    public DevTaskModel assignTask(
            @ToolParam(description = "Id of the task") Long id,
            @ToolParam(description = "Name of the person the task is assigned to") String assignedTo) {
        DevTaskModel task = devTaskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No task with id " + id));
        task.setAssignedTo(assignedTo);
        task.setUpdatedAt(Instant.now());
        return devTaskRepository.save(task);
    }

}