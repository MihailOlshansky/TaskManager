package org.molsh.security;

import lombok.RequiredArgsConstructor;
import org.molsh.dto.LoginDto;
import org.molsh.dto.RegisterDto;
import org.molsh.dto.UserDto;
import org.molsh.entity.User;
import org.molsh.exception.EntityNotFoundException;
import org.molsh.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterDto registerDto) {

        UserDto userDto = UserDto.builder()
                .username(registerDto.getUsername())
                .password(registerDto.getPassword())
                .roles(registerDto.getRoles())
                .build();

        User user = userService.create(userDto);

        return jwtTokenProvider.generateToken(user);
    }

    public String login(LoginDto request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        User user = userService.loadUserByUsername(request.getUsername());
        String token = jwtTokenProvider.generateToken(user);
        if (jwtTokenProvider.validateToken(token)) {
            SecurityContext context = SecurityContextHolder.createEmptyContext();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()
            );

            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);
        }

        return token;
    }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.loadUserByUsername(username);
    }

}
