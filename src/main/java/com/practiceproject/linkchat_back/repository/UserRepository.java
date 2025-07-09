package com.practiceproject.linkchat_back.repository;

import com.practiceproject.linkchat_back.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}