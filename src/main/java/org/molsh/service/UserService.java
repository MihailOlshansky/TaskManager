package org.molsh.service;

import jakarta.transaction.Transactional;
import java.util.Optional;
import org.molsh.common.UserRole;
import org.molsh.dto.UserDto;
import org.molsh.entity.User;
import org.molsh.exception.EntityNotFoundException;
import org.molsh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User createUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setRoles(userDto.getRoles());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(user);
    }

    public User find(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public User findNonNull(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findFirstByRole(UserRole role) {
        return userRepository.findFirstByRole(role);
    }
}
