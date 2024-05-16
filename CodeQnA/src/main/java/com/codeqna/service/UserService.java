package com.codeqna.service;

import com.codeqna.entity.Users;
import com.codeqna.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Users saveUser(Users users){
        validateDuplicateUser(users);
        return userRepository.save(users);
    }

    private void validateDuplicateUser(Users users){
        Users findUsersEmail = userRepository.findByEmail(users.getEmail());
        Users findUsersNickname = userRepository.findByNickname(users.getNickname());
        if (findUsersNickname != null){
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
        if (findUsersEmail != null){
            throw new IllegalStateException("이미 존재하는 이메일입니다. <br/>재가입을 원하시면 고객센터로 문의해주세요.");
        }
    }
}
