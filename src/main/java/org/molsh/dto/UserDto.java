package org.molsh.dto;

import lombok.Builder;
import lombok.Value;
import org.molsh.common.UserRole;

@Value
@Builder
public class UserDto {
    Long id;
    String username;
    String password;
    UserRole roles;
}
