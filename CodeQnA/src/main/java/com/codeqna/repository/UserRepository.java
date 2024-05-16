package com.codeqna.repository;

import com.codeqna.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, String> {
    Users findByNickname(String nickname);
    Users findByEmail(String email);

}
