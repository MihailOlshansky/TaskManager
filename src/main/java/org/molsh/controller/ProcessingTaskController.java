package org.molsh.controller;

import org.molsh.common.Status;
import org.molsh.dto.ProcessingTaskDto;
import org.molsh.entity.ProcessingTask;
import org.molsh.service.ProcessingTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("task/")
public class ProcessingTaskController {
    @Autowired
    private ProcessingTaskService processingTaskService;

    @PostMapping("create")
    private ProcessingTaskDto createTask(ProcessingTaskDto processingTaskDto) {

        ProcessingTask task = processingTaskService.createProcessingTask(processingTaskDto);
        return new ProcessingTaskDto(
                task.getId(),
                task.getStatus(),
                task.getPriority(),
                task.getCreatedDate(),
                task.getUser().getId()
        );
    }

    @PostMapping("change/status/{taskId}")
    private void changeStatus(@PathVariable(name = "taskId") Long taskId, @RequestBody Status status) {
        processingTaskService.changeStatus(taskId, status);
    }
}
