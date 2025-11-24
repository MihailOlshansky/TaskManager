package org.molsh.security;

import jakarta.servlet.http.HttpServletRequest;
import org.molsh.common.mapper.EntityMapper;
import org.molsh.dto.LoginDto;
import org.molsh.dto.RegisterDto;
import org.molsh.dto.UserDto;
import org.molsh.entity.User;
import org.molsh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private EntityMapper<User, UserDto> userMapper;

    @PostMapping("/register")
    private String register(@RequestBody RegisterDto registerDto) {
        return authService.register(registerDto);
    }

    @PostMapping("/login")
    private String login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @GetMapping("/me")
    private UserDto me() {
        return userMapper.entityToDto(authService.getCurrentUser());
    }
}
