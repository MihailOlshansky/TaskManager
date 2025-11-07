package org.molsh.security;

import org.molsh.common.mapper.EntityMapper;
import org.molsh.dto.UserDto;
import org.molsh.entity.User;
import org.molsh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private EntityMapper<User, UserDto> userMapper;
    @PostMapping("/register")
    UserDto register(@RequestBody UserDto userDto) {
        return userMapper.entityToDto(userService.createUser(userDto));
    }
}
