package com.codeqna.repository;

import com.codeqna.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByNickname(String nickname);
    Users findByEmail(String email);

}
