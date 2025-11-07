package org.molsh.common.mapper;

import org.molsh.dto.ProcessingTaskDto;
import org.molsh.entity.ProcessingTask;
import org.molsh.entity.User;
import org.molsh.exception.EntityNotFoundException;
import org.molsh.exception.WrongIdException;
import org.molsh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static java.util.Objects.requireNonNullElse;

@Component
public class ProcessingTaskMapper implements EntityMapper<ProcessingTask, ProcessingTaskDto>{
    @Autowired
    @Lazy
    private UserService userService;

    @Override
    public ProcessingTaskDto entityToDto(ProcessingTask task) {
        if (task == null) {
            return null;
        }
        return ProcessingTaskDto.builder()
                .id(task.getId())
                .createdDate(task.getCreatedDate())
                .priority(task.getPriority())
                .userId(task.getUser() != null ? task.getUser().getId() : null)
                .status(task.getStatus())
                .build();
    }

    @Override
    public void setNotNullProperties(ProcessingTask task, ProcessingTaskDto dto) {
        if (task == null || dto == null) {
            return;
        }

        if (!Objects.equals(task.getId(), dto.getId())) {
            throw new WrongIdException();
        }

        task.setStatus(requireNonNullElse(dto.getStatus(), task.getStatus()));
        task.setCreatedDate(requireNonNullElse(dto.getCreatedDate(), task.getCreatedDate()));
        task.setPriority(requireNonNullElse(dto.getPriority(), task.getPriority()));
        User user = dto.getUserId() != null && !dto.getUserId().equals(task.getUser().getId())
                ? userService.find(dto.getUserId())
                : task.getUser();
        if (user == null) {
            throw new EntityNotFoundException("User", dto.getUserId());
        }
        task.setUser(user);
    }
}
