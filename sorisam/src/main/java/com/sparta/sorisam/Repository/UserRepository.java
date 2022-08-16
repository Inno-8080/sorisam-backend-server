package com.sparta.sorisam.Repository;

import com.sparta.sorisam.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername (String username);

    User findUserByUsername(String username);
}
