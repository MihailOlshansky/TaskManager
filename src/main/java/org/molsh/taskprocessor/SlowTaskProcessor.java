package org.molsh.taskprocessor;

import org.molsh.common.ProcessingTaskStatus;
import org.molsh.common.UserRole;
import org.molsh.entity.ProcessingTask;
import org.molsh.exception.BadRequestException;
import org.molsh.service.ProcessingTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Component("slowTaskProcessor")
public class SlowTaskProcessor implements TaskProcessor {
    public static final int MAX_PRIORITY = 4;
    private final ConcurrentMap<Long, ProcessingTaskStatus> taskStatusCache;
    @Qualifier(value = "defaultTaskExecutor")
    private final ThreadPoolTaskExecutor executor;

    private final ProcessingTaskService processingTaskService;

    @Autowired
    SlowTaskProcessor(ThreadPoolTaskExecutor executor, ProcessingTaskService processingTaskService) {
        taskStatusCache = new ConcurrentHashMap<>();
        this.executor = executor;
        this.processingTaskService = processingTaskService;
    }

    @Override
    public void addTask(ProcessingTask task) {
        if (task.getPriority() > MAX_PRIORITY) {
            throw new BadRequestException(String.format("Priority %d is too high", task.getPriority()));
        }

        taskStatusCache.put(task.getId(), task.getStatus());
    }

    @Override
    public void addTasks(Collection<ProcessingTask> tasks) {
        taskStatusCache.putAll(tasks.stream()
                .filter(task -> task.getPriority() <= MAX_PRIORITY)
                .collect(Collectors.toMap(
                        ProcessingTask::getId, ProcessingTask::getStatus)));
    }

    @Override
    public void processTasks() {
        Set<Long> ids = taskStatusCache.entrySet().stream()
                .filter(it -> it.getValue().isNext(ProcessingTaskStatus.InProgress))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        processingTaskService.findAll(ids).stream()
                .sorted(Comparator.comparingLong(ProcessingTask::getPriority).reversed())
                .forEach(it -> executor.execute(() -> processTask(it)));


    }

    @Override
    public void processTask(ProcessingTask task) {
        if (!task.getStatus().isNext(ProcessingTaskStatus.InProgress)
                || !taskStatusCache.replace(task.getId(), task.getStatus(), ProcessingTaskStatus.InProgress)){
            System.out.printf("Task %d is processing with another thread%n", task.getId());
            return;
        }
        processingTaskService.changeStatus(task.getId(), ProcessingTaskStatus.InProgress);
        System.out.printf("Task %d is InProgress%n", task.getId());

        boolean check = task.getUser().getRoles() == UserRole.User;
        ProcessingTaskStatus newStatus = check ? ProcessingTaskStatus.Complete : ProcessingTaskStatus.Canceled;

        taskStatusCache.replace(task.getId(), task.getStatus(), newStatus);
        processingTaskService.changeStatus(task.getId(), newStatus);
        System.out.printf("Task %d is %s%n", task.getId(), newStatus);

    }
}
