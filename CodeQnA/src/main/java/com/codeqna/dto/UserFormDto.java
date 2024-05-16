package com.codeqna.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserFormDto {
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(message = "잘못된 닉네임 형식입니다.", regexp = "^[A-Za-z0-9]{2,9}$")
    private String nickname;

    @NotBlank
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(message = "잘못된 비밀번호 형식입니다.", regexp = "^[^\\W_]{8,16}$\n")
    private String password;

    private String kakao;
    private String user_condition;
}
