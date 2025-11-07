package org.molsh.security;

import org.molsh.entity.User;
import org.molsh.exception.EntityNotFoundException;
import org.molsh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService usersService;


    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = usersService.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User", username));

        return new UserDetailsImpl(user);
    }
}
