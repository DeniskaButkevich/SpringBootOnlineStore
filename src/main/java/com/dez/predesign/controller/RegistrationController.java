package com.dez.predesign.controller;

import com.dez.predesign.data.Role;
import com.dez.predesign.data.User;
import com.dez.predesign.repository.UserRepo;
import com.dez.predesign.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.UUID;

@Controller
public class RegistrationController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String userAdd(@ModelAttribute User user){
        return "register-account";
    }

    @PostMapping("/registration")
    public String userAdd(@Valid User user,
                          Errors errors,
                          Model model,
                          @RequestParam String confirmPassword){

        if (userRepo.findByUsername(user.getUsername()) != null) {
            model.addAttribute("userExist", "This name is already in use.");
            return "register-account";
        }

        if (!confirmPassword.equals(user.getPassword())) {
            model.addAttribute("errorConfirm", "passwords are not the same");
            return "register-account";
        }

        if (errors.hasErrors())
            return "register-account";

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);
        userService.sendMessage(user);

        model.addAttribute("created");
        model.addAttribute("message", "An email was sent to your email to activate the user");

        return "successfully";
    }

    @GetMapping("{code}/activate")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated)
            model.addAttribute("message", "User successfully activated");
        else
            model.addAttribute("message", "Activation code is not found!");

        return "successfully";
    }
}
