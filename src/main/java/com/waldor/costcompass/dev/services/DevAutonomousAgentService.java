package com.waldor.costcompass.dev.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.waldor.costcompass.dev.tools.DevCodeSearchTools;
import com.waldor.costcompass.dev.tools.DevMemoryTools;
import com.waldor.costcompass.dev.tools.DevTaskTools;

@Service
public class DevAutonomousAgentService {

    private final ChatClient chatClient;
    private final DevMemoryTools devMemoryTools;
    private final DevTaskTools devTaskTools;
    private final DevCodeSearchTools devCodeSearchTools;

    public DevAutonomousAgentService(ChatClient.Builder chatClientBuilder,
            DevMemoryTools devMemoryTools,
            DevTaskTools devTaskTools,
            DevCodeSearchTools devCodeSearchTools) {
        this.devMemoryTools = devMemoryTools;
        this.devTaskTools = devTaskTools;
        this.devCodeSearchTools = devCodeSearchTools;
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        You are an autonomous development assistant for the CostCompass project.
                        You are NOT being asked - your job is to independently find things
                        that need to be done and keep development tasks up to date.

                        Do the following every time you run:
                        1. Fetch all unresolved memories and all existing tasks.
                        2. Look for unresolved memories about bugs or things that should be
                           done, that are NOT already covered by an existing task.
                        3. Before creating a task: search the source code for relevant
                           keywords from the memory to find WHICH FILE the problem is likely
                           located in. Include the file path (and preferably line number) in
                           the task description. If you can't find anything likely, say so
                           instead of guessing. Then create the task with createdBy set to
                           "AI", and mark the source memory as resolved.
                        4. NEVER create a task that already seems to exist - skip it if unsure.
                        5. Always end with a short summary, or "Nothing new to do" if nothing
                           happened.
                        """)
                .build();
    }

    @Scheduled(cron = "0 0 * * * *") // every hour - adjust as needed
    public String runAutonomousScan() {
        String result = chatClient.prompt()
                .user("Run your routine check now.")
                .tools(devMemoryTools, devTaskTools, devCodeSearchTools)
                .call()
                .content();
        System.out.println("[DevAgent] " + result);
        return result;
    }
}