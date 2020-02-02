package com.dez.predesign.controller;

import com.dez.predesign.data.Role;
import com.dez.predesign.data.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @GetMapping("/admin")
    public String adminLoginGet(@AuthenticationPrincipal User user,
                                HttpServletRequest httpServletRequest,
                                Model model){

        model.addAttribute(
                "hasSession",
                httpServletRequest.getSession(false) != null
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getAuthorities().toString().contains("ROLE_ADMIN"))
            return "/admins/indexAdmin";

        return "/admin";
    }
    @PostMapping("/admin")
    public String adminLogin(){
        return "/admins/indexAdmin";
    }

    @PostMapping("/logout")
    public String logoutAdmin(){
        return ("/admin");
    }
}
