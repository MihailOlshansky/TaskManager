package org.molsh.controller;

import java.util.ArrayList;
import java.util.List;
import org.molsh.common.UserRole;
import org.molsh.common.utility.ProcessingTaskPrototype;
import org.molsh.common.ProcessingTaskStatus;
import org.molsh.common.mapper.EntityMapper;
import org.molsh.dto.ProcessingTaskDto;
import org.molsh.dto.TaskPrototypeCreationDto;
import org.molsh.entity.ProcessingTask;
import org.molsh.entity.User;
import org.molsh.service.ProcessingTaskService;
import org.molsh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserService userService;

    @Autowired
    private EntityMapper<ProcessingTask, ProcessingTaskDto> processingTaskMapper;

    @GetMapping(value = "/{taskId}",
            produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    private ProcessingTaskDto getTask(@PathVariable(name = "taskId") Long taskId) {
        return processingTaskMapper.entityToDto(processingTaskService.find(taskId));
    }


    @PostMapping(consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE},
            produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    private ProcessingTaskDto createTask(@RequestBody ProcessingTaskDto processingTaskDto) {
        ProcessingTask task = processingTaskService.create(processingTaskDto);
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
        List<Long> users = new ArrayList<>();
        userService.findFirstByRole(UserRole.User).map(User::getId).ifPresent(users::add);
        userService.findFirstByRole(UserRole.Admin).map(User::getId).ifPresent(users::add);

        for (int i = 1; i < taskAmount; i++) {
            ProcessingTask task = processingTaskService.create(ProcessingTaskDto.builder()
                    .priority(i % 11)
                    .status(ProcessingTaskStatus.Created)
                    .createdDate(LocalDateTime.now())
                    .userId(users.get(i % users.size()))
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
    public ProcessingTaskDto createHighPriorityTask(@RequestBody TaskPrototypeCreationDto dto) {
        return processingTaskMapper.entityToDto(
                processingTaskService.createHighPriorityTask(dto.getUserId()));
    }

    @GetMapping("/low-priority/prototype")
    public ProcessingTaskDto getLowPriorityTaskPrototype() {
        return processingTaskMapper.entityToDto(ProcessingTaskPrototype.lowPriorityTask());
    }
    @PostMapping("/low-priority")
    public ProcessingTaskDto createLowPriorityTask(@RequestBody TaskPrototypeCreationDto dto) {
        return processingTaskMapper.entityToDto(
                processingTaskService.createLowPriorityTask(dto.getUserId()));
    }

    @GetMapping("/default/prototype")
    public ProcessingTaskDto getDefaultTaskPrototype() {
        return processingTaskMapper.entityToDto(ProcessingTaskPrototype.defaultTask());
    }
    @PostMapping("/default")
    public ProcessingTaskDto createDefaultTask(@RequestBody TaskPrototypeCreationDto dto) {
        return processingTaskMapper.entityToDto(
                processingTaskService.createDefaultTask(dto.getUserId()));
    }
}
