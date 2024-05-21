package com.codeqna.repository;

import com.codeqna.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByNickname(String nickname);
    Users findByEmail(String email);
    boolean existsByNickname(String nickname);
}
