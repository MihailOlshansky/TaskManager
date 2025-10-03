package org.molsh.service;

import jakarta.transaction.Transactional;
import org.molsh.common.Status;
import org.molsh.dto.ProcessingTaskDto;
import org.molsh.entity.ProcessingTask;
import org.molsh.entity.User;
import org.molsh.repository.ProcessingTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProcessingTaskService {
    @Autowired
    private ProcessingTaskRepository processingTaskRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public ProcessingTask createProcessingTask(ProcessingTaskDto processingTaskDto) {
        User user = userService.find(processingTaskDto.getUserId());
        if (user == null) {
            throw new RuntimeException(String.format("User with id %d not found", processingTaskDto.getUserId()));
        }

        ProcessingTask task = new ProcessingTask();
        task.setCreatedDate(new Date());
        task.setPriority(processingTaskDto.getPriority());
        task.setStatus(Status.Created.name());
        task.setUser(user);

        return processingTaskRepository.save(task);
    }

    public void changeStatus(Long id, Status status) {
        ProcessingTask task = processingTaskRepository.findById(id).orElse(null);
        if (task == null) {
            throw new RuntimeException(String.format("Processing task with id %d not found", id));
        }
        if (!Status.valueOf(task.getStatus()).isNext(status)) {
            throw new RuntimeException(String.format("Can't change status %s to %s", task.getStatus(), status));
        }

        processingTaskRepository.updateStatus(id, status.name());
    }
}
