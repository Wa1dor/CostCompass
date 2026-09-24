package com.waldor.costcompass.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record TaskModel(Long id, String name, String description, ResourceModel resource, 
  @JsonPropertyDescription("Estimated execution time as ISO-8601 duration, e.g. PT5H for 5 hours, PT30M for 30 minutes")
  Duration estimatedExecutionTime, 
  @JsonPropertyDescription("Estimated verification time as ISO-8601 duration, e.g. PT1H30M for 1.5 hours")
  Duration estimatedVerificationTime) { 

    @JsonProperty("price")
    public BigDecimal price(){
        Duration duration = estimatedExecutionTime().plus(estimatedVerificationTime());
        BigDecimal durationMinutes = BigDecimal.valueOf(duration.toMinutes());
        BigDecimal divideMinutes = BigDecimal.valueOf(60);
        return resource.hourlyPrice().multiply(durationMinutes.divide(divideMinutes, 2, RoundingMode.HALF_UP));
    }

}
