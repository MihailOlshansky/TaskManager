package org.molsh.controller;

import org.molsh.common.mapper.EntityMapper;
import org.molsh.dto.UserDto;
import org.molsh.entity.User;
import org.molsh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EntityMapper<User, UserDto> userMapper;

    @PostMapping
    private UserDto createUser(@RequestBody UserDto userDto) {
        return userMapper.entityToDto(userService.create(userDto));
    }
    @GetMapping("/{userId}")
    private UserDto getUser(@PathVariable("userId") Long userId) {
        return userMapper.entityToDto(userService.find(userId));
    }
}
