package com.dez.predesign.controller.admin;

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
    public String adminLoginGet(HttpServletRequest request){

        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("url_prior_login", referrer);
        return "admin";
    }
}