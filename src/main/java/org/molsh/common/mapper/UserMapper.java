package org.molsh.common.mapper;

import org.molsh.dto.UserDto;
import org.molsh.entity.User;
import org.molsh.exception.WrongIdException;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static java.util.Objects.requireNonNullElse;

@Component
public class UserMapper implements EntityMapper<User, UserDto> {
    @Override
    public UserDto entityToDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roles(user.getRoles())
                .build();
    }

    @Override
    public void setNotNullProperties(User user, UserDto userDto) {
        if (user == null || userDto == null) {
            return;
        }

        if (!Objects.equals(user.getId(), userDto.getId())) {
            throw new WrongIdException();
        }

        user.setUsername(requireNonNullElse(userDto.getUsername(), user.getUsername()));
        user.setRoles(requireNonNullElse(userDto.getRoles(), user.getRoles()));
    }
}
