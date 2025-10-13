package org.molsh.controller;

import org.molsh.common.ProcessingTaskStatus;
import org.molsh.dto.ProcessingTaskDto;
import org.molsh.entity.ProcessingTask;
import org.molsh.service.ProcessingTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.LongStream;

@RestController
@RequestMapping("task/")
public class ProcessingTaskController {
    @Autowired
    private ProcessingTaskService processingTaskService;

    @PostMapping("create")
    private ProcessingTaskDto createTask(ProcessingTaskDto processingTaskDto) {
        ProcessingTask task = processingTaskService.createProcessingTask(processingTaskDto);
        return ProcessingTaskDto.builder()
                .id(task.getId())
                .status(task.getStatus())
                .priority(task.getPriority())
                .createdDate(task.getCreatedDate())
                .userId(task.getUser().getId())
                .build();
    }

    @PostMapping("change/status/{taskId}")
    private void changeStatus(@PathVariable(name = "taskId") Long taskId, @RequestBody ProcessingTaskStatus status) {
        processingTaskService.changeStatus(taskId, status);
    }

    @PostMapping("add/{taskId}")
    private void addTask(@PathVariable(name = "taskId") Long taskId) {
        processingTaskService.addTask(taskId);
    }

    @PostMapping("process/all")
    private void processTasks() {
        processingTaskService.processTasks();
    }

    @PostMapping("test")
    private void test(@RequestBody Integer taskAmount) {
        long minId = Long.MAX_VALUE;
        long maxId = 0L;
        for (int i = 1; i < taskAmount; i++) {
            ProcessingTask task = processingTaskService.createProcessingTask(ProcessingTaskDto.builder()
                    .priority(i % 11)
                    .status(ProcessingTaskStatus.Created)
                    .createdDate(LocalDateTime.now())
                    .userId(i % 2 + 1L)
                    .build());
            minId = Math.min(minId, task.getId());
            maxId = Math.max(maxId, task.getId());
        }

        processingTaskService.addTasks(LongStream.range(minId, (maxId + minId) / 2).boxed().toList());
        processingTaskService.processTasks();
        processingTaskService.addTasks(LongStream.range((maxId + minId) / 2, maxId + 1).boxed().toList());
        processingTaskService.processTasks();
    }
}
