package com.waldor.costcompass.events.models;

import com.waldor.costcompass.models.ResourceModel;

public record ResourceEvent(ResourceEventType resourceEventType, ResourceModel resourceModel) {

}
