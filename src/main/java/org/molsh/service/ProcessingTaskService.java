package org.molsh.service;

import org.molsh.common.mapper.EntityMapper;
import org.molsh.common.utility.ProcessingTaskPrototype;
import org.molsh.common.ProcessingTaskStatus;
import org.molsh.dto.ProcessingTaskDto;
import org.molsh.entity.ProcessingTask;
import org.molsh.entity.User;
import org.molsh.exception.BadRequestException;
import org.molsh.exception.EntityNotFoundException;
import org.molsh.exception.ProcessingTaskStatusOrderException;
import org.molsh.repository.ProcessingTaskRepository;
import org.molsh.taskprocessor.TaskProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public final class ProcessingTaskService extends EntityService<ProcessingTask, ProcessingTaskDto>{
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
    private EntityMapper<ProcessingTask, ProcessingTaskDto> processingTaskMapper;

    public ProcessingTask updateProcessingTask(ProcessingTaskDto processingTaskDto) {
        if (processingTaskDto.getId() == null) {
            throw new BadRequestException("No id found");
        }

        ProcessingTask processingTask = processingTaskRepository.findById(processingTaskDto.getId())
                .orElseThrow(() ->new EntityNotFoundException("ProcessingTask", processingTaskDto.getId()));

        processingTaskMapper.setNotNullProperties(processingTask, processingTaskDto);
        processingTaskRepository.save(processingTask);

        return processingTask;

    }

    public void changeStatus(Long id, ProcessingTaskStatus status) {
        ProcessingTask task = processingTaskRepository.findById(id).orElse(null);
        if (task == null) {
            throw new EntityNotFoundException("ProcessingTask", id);
        }
        if (!task.getStatus().isNext(status)) {
            throw new ProcessingTaskStatusOrderException(task.getStatus(), status);
        }

        processingTaskRepository.updateStatus(id, status);
    }

    @Override
    protected JpaRepository<ProcessingTask, Long> getRepository() {
        return processingTaskRepository;
    }

    @Override
    public String getEntityName() {
        return "ProcessingTask";
    }

    @Override
    public ProcessingTask create(ProcessingTaskDto processingTaskDto) {
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
