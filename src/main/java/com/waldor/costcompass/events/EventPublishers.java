package com.waldor.costcompass.events;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventPublishers {
  private final KafkaTemplate<String, Object> kafkaTemplate;
  
  public EventPublishers(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void publish(String topic, Object event) {
    kafkaTemplate.send(topic, event);
  }
}
