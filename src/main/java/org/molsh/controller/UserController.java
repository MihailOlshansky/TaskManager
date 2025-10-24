package org.molsh.controller;

import org.molsh.common.mapper.EntityMapper;
import org.molsh.dto.UserDto;
import org.molsh.entity.User;
import org.molsh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @Qualifier("userMapper")
    private EntityMapper<User, UserDto> userMapper;

    @PostMapping
    private UserDto createUser(@RequestBody UserDto userDto) {
        return userMapper.entityToDto(userService.createUser(userDto));
    }
    @GetMapping("/{userId}")
    private UserDto getUser(@PathVariable("userId") Long userId) {
        return userMapper.entityToDto(userService.find(userId));
    }
}
