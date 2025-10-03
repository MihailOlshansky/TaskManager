package org.molsh.dto;

import lombok.Value;

import java.util.Set;
@Value
public class UserDto {
    Long id;
    String username;
    String roles;
    Set<ProcessingTaskDto> tasks;
}
