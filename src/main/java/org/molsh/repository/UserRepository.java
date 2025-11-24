package org.molsh.repository;

import java.util.Optional;
import org.molsh.common.UserRole;
import org.molsh.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndPassword(String username, String password);
    Optional<User> findFirstByRoles(UserRole role);
}
