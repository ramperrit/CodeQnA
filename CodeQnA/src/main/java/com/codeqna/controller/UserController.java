package com.codeqna.controller;

import com.codeqna.dto.UserFormDto;
import com.codeqna.entity.Users;
import com.codeqna.repository.UserRepository;
import com.codeqna.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/users")
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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


    @GetMapping("/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "이메일 또는 비밀번호를 확인해주세요");
        return "/user/login";
    }

    @GetMapping("/mypage")
    public String mypage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Users users = userRepository.findByEmail(email);
        model.addAttribute("nickname", users.getNickname());
        model.addAttribute("email",users.getEmail());
        model.addAttribute("regdate",users.getRegdate());
        return "user/mypage";
    }

    @GetMapping("/{nickname}/exist")
    public ResponseEntity<Map<String, Boolean>> checkNicknameExists(@PathVariable String nickname) {
        boolean exists = userService.nicknameExists(nickname);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/checkNickname")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestBody Map<String, String> request) {
        String nickname = request.get("nickname");
        boolean exists = userService.nicknameExists(nickname);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/updateNick")
    public ResponseEntity<String> updateNickname(@RequestParam String nickname){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        boolean updateSuccess = userService.updateNickname(email, nickname);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/Loginmain"));
        if (updateSuccess){
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("닉네임변경 오류");
        }
    }

    @GetMapping("/currentNickname")
    public ResponseEntity<String> getCurrentNickname() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        String nickname = userService.getNicknameByEmail(email);
        return ResponseEntity.ok(nickname);
    }


}
