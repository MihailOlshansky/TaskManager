package org.molsh.dto;

import lombok.Builder;
import lombok.Value;
import org.molsh.common.UserRole;

import java.util.Set;
@Value
@Builder
public class UserDto {
    Long id;
    String username;
    UserRole roles;
    Set<ProcessingTaskDto> tasks;
}
