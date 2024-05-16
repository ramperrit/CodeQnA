package com.codeqna.entity;


import com.codeqna.constant.UserRole;
import com.codeqna.dto.UserFormDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {


//    private Long id;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;

    @Column(name = "password")
    private String password;

    @Column(name = "user_role", nullable = false)
    private UserRole user_role;

    @Column(name = "kakao", columnDefinition = "VARCHAR(5) DEFAULT 'N' ")
    private String kakao;

    @CreatedDate
    @Column(name = "regdate")
    private LocalDateTime regdate;

    @Column(name = "user_condition", nullable = false, columnDefinition = "VARCHAR(5) DEFAULT 'Y' ")
    private String user_condition;


//    회원가입
    public static Users createUsers(UserFormDto userFormDto, PasswordEncoder passwordEncoder){
        Users users = new Users();
        users.builder()
                .nickname(userFormDto.getNickname())
                .email(userFormDto.getEmail())
                .password(passwordEncoder.encode(userFormDto.getPassword()))
                .user_role(UserRole.USER)
                .user_condition(userFormDto.getUser_condition())
                .kakao(userFormDto.getKakao())
                .build();
        return users;
    }
}
