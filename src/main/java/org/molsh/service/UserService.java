package org.molsh.service;

import jakarta.transaction.Transactional;
import org.molsh.dto.UserDto;
import org.molsh.entity.User;
import org.molsh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User createUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setRoles(userDto.getRoles());
        return userRepository.save(user);
    }

    public User find(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
