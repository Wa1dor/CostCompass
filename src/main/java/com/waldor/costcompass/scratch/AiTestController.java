package com.waldor.costcompass.scratch;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/costcompass")
public class AiTestController {

  private final ChatClient chatClient;

  public AiTestController(ChatClient.Builder builder) {
    this.chatClient = builder.build();
  }

  @GetMapping("/ask")
  public String ask(@RequestParam String question) {
    return chatClient.prompt()
        .user(question)
        .call()
        .content();
  }

}
