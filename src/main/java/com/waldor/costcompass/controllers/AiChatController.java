package com.waldor.costcompass.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.waldor.costcompass.models.AiChatRequestModel;
import com.waldor.costcompass.models.AiChatResponseModel;
import com.waldor.costcompass.services.AiChatService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/costcompass/assistant")
public class AiChatController {

  private final AiChatService aiChatService;

  public AiChatController(AiChatService aiChatService) {
    this.aiChatService = aiChatService;
  }

  @PostMapping("/request")
  public AiChatResponseModel request(@RequestBody AiChatRequestModel aiChatRequestModel) {
    return aiChatService.response(aiChatRequestModel);
  }
}
