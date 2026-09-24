package com.waldor.costcompass.dev.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.waldor.costcompass.models.AiChatRequestModel;
import com.waldor.costcompass.models.AiChatResponseModel;
import com.waldor.costcompass.dev.services.AiDevChatService;

@RestController
@RequestMapping("/api/costcompass/dev/assistant")
public class AiDevChatController {

    private AiDevChatService aiDevChatService;

    public AiDevChatController(AiDevChatService aiDevChatService) {
        this.aiDevChatService = aiDevChatService;
    }

    @PostMapping("/request")
    public AiChatResponseModel request(@RequestBody AiChatRequestModel aiChatRequestModel) {
        return aiDevChatService.response(aiChatRequestModel);
    }

}
