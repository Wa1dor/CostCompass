package com.waldor.costcompass.models;

import java.math.BigDecimal;
import java.util.List;

public record ResourceModel(Long id, List<RoleModel> roles, BigDecimal hourlyPrice, ResourceType type){
    enum ResourceType{
        HUMAN,
        LLM
    }
}