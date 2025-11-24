package org.molsh.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoginDto {
    String username;
    String password;
}
