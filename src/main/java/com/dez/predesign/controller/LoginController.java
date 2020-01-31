package com.dez.predesign.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("admin")
    public String adminLoginGet(){

        return "/admin";
    }

    @PostMapping("admin")
        public String adminLogin(){

        return "/admins/indexAdmin";
    }
}
