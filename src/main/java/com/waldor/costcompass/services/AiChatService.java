package com.waldor.costcompass.services;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import com.waldor.costcompass.models.AiChatRequestModel;
import com.waldor.costcompass.models.AiChatResponseModel;
import com.waldor.costcompass.models.ProjectModel;
import com.waldor.costcompass.models.AiChatRequestModel.ChatTurn;
import com.waldor.costcompass.models.AiChatRequestModel.SenderType;
import com.waldor.costcompass.tools.ProjectTools;
import com.waldor.costcompass.tools.ResourceTools;
import com.waldor.costcompass.tools.RoleTools;
import com.waldor.costcompass.tools.TaskTools;

@Service
public class AiChatService {
  private final ChatClient chatClient;
  private final ProjectService projectService;
  private final ProjectTools projectTools;
  private final ResourceTools resourceTools;
  private final TaskTools taskTools;
  private final RoleTools roleTools;

  public AiChatService(ChatClient.Builder builder, ProjectService projectService, ProjectTools projectTools,
      ResourceTools resourceTools, TaskTools taskTools, RoleTools roleTools) {
    this.chatClient = builder.build();
    this.projectService = projectService;
    this.projectTools = projectTools;
    this.resourceTools = resourceTools;
    this.taskTools = taskTools;
    this.roleTools = roleTools;
  }

  public String context(AiChatRequestModel aiChatRequestModel) {
    StringBuilder sb = new StringBuilder();
    ProjectModel project = projectService.getProjectById(aiChatRequestModel.projectId());
    sb.append("""
          Rules:
            - Answer in the same language as the user.
        """).append("\n");

    if (project == null) {
      sb.append("""
            User has given Project id null, Project do not exist.
            Help the user to create a new project.
          """).append("\n");

      System.out.println(sb.toString());
      return sb.toString();
    }
    sb.append("Project: ").append(project.getProjectName()).append("\n");
    sb.append("Customer name: ").append(project.getCustomer()).append("\n");
    sb.append("Tasks: ").append(project.getTasks()).append("\n");
    System.out.println(sb.toString());
    return sb.toString();
  }

  public AiChatResponseModel response(AiChatRequestModel aiChatRequestModel) {
    projectTools.setAction(null);
    List<ChatTurn> history = aiChatRequestModel.history();
    List<Message> message = history.stream()
        .map(turn -> turn.sender() == SenderType.USER ? (Message) new UserMessage(turn.text())
            : new AssistantMessage(turn.text()))
        .toList();
    String chatResponse = chatClient.prompt()
        .system(context(aiChatRequestModel))
        .tools(projectTools, resourceTools, taskTools, roleTools)
        .messages(message)
        .user(aiChatRequestModel.message())
        .call()
        .content();
    return new AiChatResponseModel(chatResponse, projectTools.getAction());
  }
}