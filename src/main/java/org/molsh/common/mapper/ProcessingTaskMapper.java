package org.molsh.common.mapper;

import org.molsh.dto.ProcessingTaskDto;
import org.molsh.entity.ProcessingTask;
import org.molsh.entity.User;
import org.molsh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static java.util.Objects.requireNonNullElse;

@Component
@Qualifier("processingTaskMapper")
public class ProcessingTaskMapper implements EntityMapper<ProcessingTask, ProcessingTaskDto>{
    @Autowired
    private UserService userService;

    @Override
    public ProcessingTaskDto entityToDto(ProcessingTask task) {
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
        if (!Objects.equals(task.getId(), dto.getId())) {
            throw new RuntimeException("Entity and dto have different ids");
        }

        task.setStatus(requireNonNullElse(dto.getStatus(), task.getStatus()));
        task.setCreatedDate(requireNonNullElse(dto.getCreatedDate(), task.getCreatedDate()));
        task.setPriority(requireNonNullElse(dto.getPriority(), task.getPriority()));
        User user = dto.getUserId() != null && !dto.getUserId().equals(task.getUser().getId())
                ? userService.find(dto.getId())
                : task.getUser();
        if (user == null) {
            throw new RuntimeException(String.format("User with id %d not found", dto.getUserId()));
        }
        task.setUser(user);
    }
}
