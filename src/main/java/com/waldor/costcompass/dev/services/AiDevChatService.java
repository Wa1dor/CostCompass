package com.waldor.costcompass.dev.services;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import com.waldor.costcompass.dev.tools.DevCodeSearchTools;
import com.waldor.costcompass.dev.tools.DevMemoryTools;
import com.waldor.costcompass.dev.tools.DevTaskTools;
import com.waldor.costcompass.models.AiChatRequestModel;
import com.waldor.costcompass.models.AiChatResponseModel;
import com.waldor.costcompass.models.AiChatRequestModel.ChatTurn;
import com.waldor.costcompass.models.AiChatRequestModel.SenderType;

@Service
public class AiDevChatService {

	private ChatClient chatClient;
	private DevMemoryTools devMemoryTools;
	private DevTaskTools devTaskTools;
	private DevCodeSearchTools devCodeSearchTools;

	public AiDevChatService(ChatClient.Builder builder, DevMemoryTools devMemoryTools, DevTaskTools devTaskTools,
			DevCodeSearchTools devCodeSearchTools) {
		this.chatClient = builder.build();
		this.devMemoryTools = devMemoryTools;
		this.devTaskTools = devTaskTools;
		this.devCodeSearchTools = devCodeSearchTools;
	}

	private String context() {
		return """
				You are a development assistant for the costcompass project.
				You help the team remember tasks, decisions and bugs across sessions.
				Use your tools to save and recall memories about the project's development.

				Always reply in the same language the user writes to you in.

				If you are asked for improvement suggestions: use listProjectFiles and
				readFile to examine BOTH the backend (Java controllers, services and
				models) and the frontend (TSX components) before answering - don't limit
				yourself to one side. Consider two kinds of suggestions: improvements to
				code that already exists, AND functionality that does not exist yet but
				would clearly benefit a project cost management tool like this one (e.g.
				missing endpoints, missing reports, missing user-facing features). State
				explicitly which of the two a suggestion is. Propose 2-4 concrete,
				well-reasoned suggestions in plain text in the chat. Do NOT create tasks
				automatically for these - wait until the user says which ideas should be
				saved, and then use createTask with type FEATURE.

				If you are asked whether a task is done or fixed: do not just report the
				status field from the database. Look at the task's description for file
				references, or use searchCode with keywords from the title, then use
				readFile to actually read the relevant code and check whether the
				problem still exists. Explain what you found and why. Only update the
				task's status with updateTaskStatus if the user explicitly confirms they
				want that change.
				""";
	}

	public AiChatResponseModel response(AiChatRequestModel aichatRequestModel) {
		List<ChatTurn> history = aichatRequestModel.history();
		List<Message> messages = history.stream()
				.map(turn -> turn.sender() == SenderType.USER ? (Message) new UserMessage(turn.text())
						: new AssistantMessage(turn.text()))
				.toList();
		String chatResponse = chatClient.prompt()
				.system(context())
				.messages(messages)
				.tools(devMemoryTools, devTaskTools, devCodeSearchTools)
				.user(aichatRequestModel.message())
				.call()
				.content();
		return new AiChatResponseModel(chatResponse, null);
	}
}