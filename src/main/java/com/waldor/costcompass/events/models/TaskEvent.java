package com.waldor.costcompass.events.models;

import com.waldor.costcompass.models.TaskModel;

public record TaskEvent(TaskEventType taskEventType, TaskModel taskModel

) {
  
}
