package org.molsh.common.mapper;

import org.molsh.dto.UserDto;
import org.molsh.entity.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static java.util.Objects.requireNonNullElse;

@Component
@Qualifier("userMapper")
public class UserMapper implements EntityMapper<User, UserDto> {
    @Override
    public UserDto entityToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roles(user.getRoles())
                .build();
    }

    @Override
    public void setNotNullProperties(User user, UserDto userDto) {
        if (!Objects.equals(user.getId(), userDto.getId())) {
            throw new RuntimeException("Entity and dto have different ids");
        }

        user.setUsername(requireNonNullElse(userDto.getUsername(), user.getUsername()));
        user.setRoles(requireNonNullElse(userDto.getRoles(), user.getRoles()));
    }
}
