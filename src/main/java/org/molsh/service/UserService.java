package org.molsh.service;

import java.util.Optional;
import org.molsh.common.UserRole;
import org.molsh.dto.UserDto;
import org.molsh.entity.User;
import org.molsh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public final class UserService extends EntityService<User, UserDto>{
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
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findFirstByRole(UserRole role) {
        return userRepository.findFirstByRoles(role);
    }
}
