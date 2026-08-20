package com.waldor.costcompass.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

public record TaskModel(Long id, String description, ResourceModel resourceModel, Duration estimatedExecutionTime, Duration estimatedVerificationTime) { 

    public BigDecimal price(){
        Duration duration = estimatedExecutionTime().plus(estimatedVerificationTime());
        BigDecimal durationMinutes = BigDecimal.valueOf(duration.toMinutes());
        BigDecimal divideMinutes = BigDecimal.valueOf(60);
        return resourceModel.hourlyPrice().multiply(durationMinutes.divide(divideMinutes, 2, RoundingMode.HALF_UP));
    }

}
