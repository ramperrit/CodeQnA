package com.codeqna.controller;

import com.codeqna.dto.UserFormDto;
import com.codeqna.entity.Users;
import com.codeqna.repository.UserRepository;
import com.codeqna.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/checkNickname")
    public boolean checkNickname(@RequestParam("nickname") String nickname){
        return userService.isNicknameExist(nickname);
    }

    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("userFormDto", new UserFormDto());
        return "/user/signup";
    }

    @GetMapping("/login")
    public String login(){
        return "/user/login";
    }

    @PostMapping("/signup")
    public String userForm(@Valid UserFormDto userFormDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "/user/signup";
        }
        try {
            Users users = Users.createUsers(userFormDto, passwordEncoder);
            userService.saveUser(users);
        }catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "/user/signup";
        }
        return "redirect:/users/login";
    }

    @GetMapping("/signup/error")
    public String signupError(Model model){
        model.addAttribute("signupErrorMsg", "이메일 또는 비밀번호를 확인해주세요");
        return "/user/signup";
    }

    @GetMapping("/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "이메일 또는 비밀번호를 확인해주세요");
        return "/user/login";
    }

}
