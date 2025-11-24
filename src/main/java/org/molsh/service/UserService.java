package org.molsh.service;

import java.util.Optional;
import org.molsh.common.UserRole;
import org.molsh.dto.UserDto;
import org.molsh.entity.User;
import org.molsh.exception.BadRequestException;
import org.molsh.exception.EntityNotFoundException;
import org.molsh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends EntityService<User, UserDto> implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected JpaRepository<User, Long> getRepository() {
        return userRepository;
    }

    @Override
    public String getEntityName() {
        return "User";
    }

    @Override
    public User create(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setRoles(userDto.getRoles());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new BadRequestException(String.format("Error creating user %s", user.getUsername()));
        }
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findFirstByRole(UserRole role) {
        return userRepository.findFirstByRoles(role);
    }

    @Override
    public User loadUserByUsername(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User", username));
    }
}
