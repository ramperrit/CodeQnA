package com.codeqna.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ViewController {

    @GetMapping("/main")
    public String mainpage(){
        return "mainpage";
    }

    @GetMapping("/login")
    public String login(){
        return "user/login";
    }

    @GetMapping("/signup")
    public String signup(){
        return "user/signup";
    }

    @GetMapping("/admin/deleted")
    public String deletedBoard(){
        return "admin/deletedBoards";
    }
}
