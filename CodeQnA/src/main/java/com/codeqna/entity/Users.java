package com.codeqna.entity;


import com.codeqna.constant.UserRole;
import com.codeqna.dto.UserFormDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@DynamicInsert
public class Users {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole user_role;

    @Column(name = "kakao", columnDefinition = "VARCHAR(5) DEFAULT 'N' ")
    private String kakao;

    @CreatedDate
    @Column(name = "regdate")
    private LocalDateTime regdate;

    @Column(name = "user_condition", columnDefinition = "VARCHAR(5) DEFAULT 'Y' ")
    private String user_condition;


//    회원가입
    public static Users createUsers(UserFormDto userFormDto, PasswordEncoder passwordEncoder){
        return Users.builder()
                .nickname(userFormDto.getNickname())
                .email(userFormDto.getEmail())
                .password(passwordEncoder.encode(userFormDto.getPassword()))
                .user_role(UserRole.USER)
                .user_condition(userFormDto.getUser_condition())
                .kakao(userFormDto.getKakao())
                .build();
    }
}
