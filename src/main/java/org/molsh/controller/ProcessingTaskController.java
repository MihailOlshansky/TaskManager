package org.molsh.controller;

import org.molsh.common.ProcessingTaskPrototype;
import org.molsh.common.ProcessingTaskStatus;
import org.molsh.common.mapper.EntityMapper;
import org.molsh.dto.ProcessingTaskDto;
import org.molsh.entity.ProcessingTask;
import org.molsh.service.ProcessingTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.LongStream;

import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("task")
public class ProcessingTaskController {
    @Autowired
    private ProcessingTaskService processingTaskService;

    @Autowired
    @Qualifier("processingTaskMapper")
    private EntityMapper<ProcessingTask, ProcessingTaskDto> processingTaskMapper;

    @GetMapping(value = "/{taskId}",
            produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    private ProcessingTaskDto getTask(@PathVariable(name = "taskId") Long taskId) {
        return processingTaskService.find(taskId)
                .map(processingTaskMapper::entityToDto)
                .orElse(null);
    }


    @PostMapping(consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE},
            produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    private ProcessingTaskDto createTask(@RequestBody ProcessingTaskDto processingTaskDto) {
        ProcessingTask task = processingTaskService.createProcessingTask(processingTaskDto);
        return processingTaskMapper.entityToDto(task);
    }

    @PutMapping(consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE},
            produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    private ProcessingTaskDto updateTask(@RequestBody ProcessingTaskDto processingTaskDto) {
        ProcessingTask task = processingTaskService.updateProcessingTask(processingTaskDto);
        return processingTaskMapper.entityToDto(task);
    }
    @PutMapping("/status/{taskId}")
    private void changeStatus(@PathVariable(name = "taskId") Long taskId, @RequestBody ProcessingTaskStatus status) {
        processingTaskService.changeStatus(taskId, status);
    }

    @PostMapping("/add/{taskId}")
    private void addTask(@PathVariable(name = "taskId") Long taskId) {
        processingTaskService.addTask(taskId);
    }

    @PostMapping("/process")
    private void processTasks() {
        processingTaskService.processTasks();
    }

    @PostMapping("/test")
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

    @GetMapping("/high-priority/prototype")
    public ProcessingTaskDto getHighPriorityTaskPrototype() {
        return processingTaskMapper.entityToDto(ProcessingTaskPrototype.highPriorityTask());
    }
    @PostMapping("/high-priority")
    public ProcessingTaskDto createHighPriorityTask(@RequestBody Long userId) {
        return processingTaskMapper.entityToDto(
                processingTaskService.createHighPriorityTask(userId));
    }

    @GetMapping("/low-priority/prototype")
    public ProcessingTaskDto getLowPriorityTaskPrototype() {
        return processingTaskMapper.entityToDto(ProcessingTaskPrototype.lowPriorityTask());
    }
    @PostMapping("/low-priority")
    public ProcessingTaskDto createLowPriorityTask(@RequestBody Long userId) {
        return processingTaskMapper.entityToDto(
                processingTaskService.createLowPriorityTask(userId));
    }

    @GetMapping("/default/prototype")
    public ProcessingTaskDto getDefaultTaskPrototype() {
        return processingTaskMapper.entityToDto(ProcessingTaskPrototype.defaultTask());
    }
    @PostMapping("/default")
    public ProcessingTaskDto createDefaultTask(@RequestBody Long userId) {
        return processingTaskMapper.entityToDto(
                processingTaskService.createDefaultTask(userId));
    }
}
