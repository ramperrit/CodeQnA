package com.codeqna.service;

import com.codeqna.entity.Users;
import com.codeqna.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public Users saveUser(Users users){
        validateDuplicateUser(users);
        return userRepository.save(users);
    }

    private void validateDuplicateUser(Users users){
        Users findUsersNickname = userRepository.findByNickname(users.getNickname());
        Users findUsersEmail = userRepository.findByEmail(users.getEmail());
        if (findUsersNickname != null){
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
        if (findUsersEmail != null){
            throw new IllegalStateException("이미 존재하는 이메일입니다. 재가입을 원하시면 고객센터로 문의해주세요.");
        }
    }

    public boolean isNicknameExist(String nickname){
        Users existNickname = userRepository.findByNickname(nickname);
        return existNickname == null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users = userRepository.findByEmail(email);
        if (users == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(users.getEmail())
                .password(users.getPassword())
                .roles(users.getUser_role().toString())
                .build();
    }

//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Users users = userRepository.findByEmail(email);
//        if (users == null){
//            throw new UsernameNotFoundException(email);
//        }
//
//        GrantedAuthority authority = new SimpleGrantedAuthority(users.getUser_role().toString());
//
//        return new User(
//                users.getEmail(),
//                users.getPassword(),
//                Collections.singletonList(authority)
//        );
//    }
}
