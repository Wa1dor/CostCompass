package com.waldor.costcompass.models;

import java.util.List;

public record AiChatRequestModel(String message, List<ChatTurn> history, Long projectId) {
  public enum SenderType {
    USER,
    ASSISTANT
  }
  
  public record ChatTurn(SenderType sender, String text) {
  }
}