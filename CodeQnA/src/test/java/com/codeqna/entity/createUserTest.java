package com.codeqna.entity;

import com.codeqna.dto.UserFormDto;
import com.codeqna.repository.UserRepository;
import com.codeqna.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class createUserTest {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public Users createUsers(){
        UserFormDto userFormDto = new UserFormDto();
        userFormDto.setNickname("test001");
        userFormDto.setEmail("test123@test.com");
        userFormDto.setPassword("123123123");
        return Users.createUsers(userFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("일반 회원가입 테스트")
    public void saveUserTest(){

        Users users = createUsers();

        Users savedUser = userService.saveUser(users);

        assertEquals(users.getEmail(), savedUser.getEmail());
        assertEquals(users.getNickname(), savedUser.getNickname());
        assertEquals(users.getPassword(), savedUser.getPassword());

    }

}