package org.molsh.service;

import jakarta.transaction.Transactional;
import org.molsh.common.ProcessingTaskPrototype;
import org.molsh.common.ProcessingTaskStatus;
import org.molsh.common.mapper.ProcessingTaskMapper;
import org.molsh.dto.ProcessingTaskDto;
import org.molsh.entity.ProcessingTask;
import org.molsh.entity.User;
import org.molsh.repository.ProcessingTaskRepository;
import org.molsh.taskprocessor.TaskProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProcessingTaskService {
    public static Integer MIN_PRIORITY = 0;
    public static Integer MAX_PRIORITY = 10;

    @Autowired
    private ProcessingTaskRepository processingTaskRepository;
    @Autowired
    private UserService userService;
    @Lazy
    @Autowired
    @Qualifier("fastTaskProcessor")
    private TaskProcessor fastTaskProcessor;
    @Lazy
    @Autowired
    @Qualifier("slowTaskProcessor")
    private TaskProcessor slowTaskProcessor;

    @Autowired
    private ProcessingTaskMapper processingTaskMapper;

    @Transactional
    public ProcessingTask createProcessingTask(ProcessingTaskDto processingTaskDto) {
        User user = userService.findNonNull(processingTaskDto.getUserId());

        ProcessingTask task = ProcessingTask.builder()
                .priority(processingTaskDto.getPriority())
                .createdDate(processingTaskDto.getCreatedDate() != null
                        ? processingTaskDto.getCreatedDate()
                        : LocalDateTime.now())
                .status(ProcessingTaskStatus.Created)
                .user(user)
                .build();

        return processingTaskRepository.save(task);
    }

    public ProcessingTask updateProcessingTask(ProcessingTaskDto processingTaskDto) {
        if (processingTaskDto.getId() == null) {
            throw new RuntimeException("No id found");
        }

        ProcessingTask processingTask = processingTaskRepository.findById(processingTaskDto.getId())
                .orElseThrow(() ->new RuntimeException(
                        String.format("No task with id %d found", processingTaskDto.getId())));

        processingTaskMapper.setNotNullProperties(processingTask, processingTaskDto);
        processingTaskRepository.save(processingTask);

        return processingTask;

    }

    public void changeStatus(Long id, ProcessingTaskStatus status) {
        ProcessingTask task = processingTaskRepository.findById(id).orElse(null);
        if (task == null) {
            throw new RuntimeException(String.format("Processing task with id %d not found", id));
        }
        if (!task.getStatus().isNext(status)) {
            throw new RuntimeException(String.format("Can't change status %s to %s", task.getStatus(), status));
        }

        processingTaskRepository.updateStatus(id, status);
    }

    public Optional<ProcessingTask> find(Long id) {
        return processingTaskRepository.findById(id);
    }

    public List<ProcessingTask> findAll(Iterable<Long> ids) {
        return processingTaskRepository.findAllById(ids);
    }

    public void addTask(Long id) {
        addTasks(List.of(id));
    }

    public void addTasks(Iterable<Long> ids) {
        List<ProcessingTask> tasks = processingTaskRepository.findAllById(ids);
        slowTaskProcessor.addTasks(tasks);
        fastTaskProcessor.addTasks(tasks);
    }

    public void processTasks() {
        fastTaskProcessor.processTasks();
        slowTaskProcessor.processTasks();
    }

    public ProcessingTask createHighPriorityTask(Long userId)
    {
        ProcessingTask task = ProcessingTaskPrototype.highPriorityTask();
        task.setUser(userService.findNonNull(userId));

        return processingTaskRepository.save(task);
    }

    public ProcessingTask createLowPriorityTask(Long userId)
    {
        ProcessingTask task = ProcessingTaskPrototype.lowPriorityTask();
        task.setUser(userService.findNonNull(userId));

        return processingTaskRepository.save(task);
    }

    public ProcessingTask createDefaultTask(Long userId)
    {
        ProcessingTask task = ProcessingTaskPrototype.defaultTask();
        task.setUser(userService.findNonNull(userId));

        return processingTaskRepository.save(task);
    }
}
