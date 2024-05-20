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
    public Users createUsers2(){
        UserFormDto userFormDto = new UserFormDto();
        userFormDto.setNickname("test001");
        userFormDto.setEmail("test456@test.com");
        userFormDto.setPassword("123123123");
        return Users.createUsers(userFormDto, passwordEncoder);
    }

    public Users createUsers3(){
        UserFormDto userFormDto = new UserFormDto();
        userFormDto.setNickname("test002");
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

    @Test
    @DisplayName("중복 가입 테스트_닉네임")
    public void saveDuplicateNicknameTest(){
        Users users1 = createUsers();
        Users users2 = createUsers2();
        userService.saveUser(users1);

        Throwable e = assertThrows(IllegalStateException.class, () -> {
            userService.saveUser(users2);
        });

        assertEquals("이미 존재하는 닉네임입니다.", e.getMessage());
    }

    @Test
    @DisplayName("중복 가입 테스트_이메일")
    public void saveDuplicateEmailTest(){
        Users users1 = createUsers();
        Users users2 = createUsers3();
        userService.saveUser(users1);

        Throwable e = assertThrows(IllegalStateException.class, () -> {
            userService.saveUser(users2);
        });

        assertEquals("이미 존재하는 이메일입니다. 재가입을 원하시면 고객센터로 문의해주세요.", e.getMessage());
    }
}