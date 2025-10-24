package org.molsh.common;

import org.molsh.entity.ProcessingTask;
import org.molsh.service.ProcessingTaskService;

import java.time.LocalDateTime;

public interface ProcessingTaskPrototype {
    static ProcessingTask highPriorityTask() {
        return ProcessingTask.builder()
                .createdDate(LocalDateTime.now())
                .priority(ProcessingTaskService.MAX_PRIORITY)
                .status(ProcessingTaskStatus.Created)
                .build();
    }

    static ProcessingTask lowPriorityTask() {
        return ProcessingTask.builder()
                .createdDate(LocalDateTime.now())
                .priority(ProcessingTaskService.MIN_PRIORITY)
                .status(ProcessingTaskStatus.Created)
                .build();
    }

    static ProcessingTask defaultTask() {
        return ProcessingTask.builder()
                .createdDate(LocalDateTime.now())
                .priority((ProcessingTaskService.MAX_PRIORITY + ProcessingTaskService.MIN_PRIORITY) / 2)
                .status(ProcessingTaskStatus.Created)
                .build();
    }
}
