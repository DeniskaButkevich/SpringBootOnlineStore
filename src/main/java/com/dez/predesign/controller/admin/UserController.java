package com.dez.predesign.controller.admin;

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
import java.util.*;

@Controller
public class UserController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @ModelAttribute(name="users")
    public Iterable<User> users() {
        return userRepo.findAll();
    }

    @ModelAttribute(name="roles")
    public Role[] roles(){
        return Role.values();
    }

    @GetMapping("/admins/users/add")
    public String userAdd(@ModelAttribute User user){
        return "admins/userAdd";
    }

    @GetMapping("/admins/users")
    public String usersShow(@ModelAttribute User user, Model model){

        final String[] freeze = {"admin", "1"};
        model.addAttribute("freeze", freeze);
        return "admins/userList";
    }

    @GetMapping("/admins/user/{id}")
    public String oneUser(@PathVariable String id, Model model){
        model.addAttribute("user", userRepo.findById(Integer.parseInt(id)).get());
        final String[] freeze = {"admin", "1"};
        model.addAttribute("freeze", freeze);
        return "admins/userOne";
    }

    @GetMapping("/admins/users/edit/{id}")
    public String userEdit(@PathVariable String id,Model model){

        User usr = userRepo.findById(Integer.parseInt(id)).get();
        model.addAttribute("user",usr);

        return "admins/userEdit";
    }

    @GetMapping("/admins/users/passwordChange/{id}")
    public String userPasswordChange(@PathVariable String id, Model model) {
        return "admins/passwordChange";
    }

    @GetMapping("/admins/delete")
    public String userRemove(@RequestParam String id) {

        userRepo.delete(
                userRepo.findById(
                        Integer.parseInt(id)).get());

        return "redirect:/admins/users";
    }

    @PostMapping("/admins/users/edit/{id}")
    public String userEdit(@RequestParam Map<String, String> form,
                           @PathVariable String id,
                           @Valid User user,
                           Errors errors,
                           Model model){

        if(userService.save(id,form,user,model,errors)) {
            return "redirect:/admins/users";
        } else{
            return "admins/userEdit";
        }
    }

    @PostMapping("/admins/users/passwordChange/{id}")
    public String userPasswordChange(@RequestParam String confirmPassword,
                                     @PathVariable String id,
                                     @RequestParam String password,
                                     Model model) {
        if(userService.passwordChange(id,password,confirmPassword,model)){
           return "redirect:/admins/users";
        } else{
            return "admins/passwordChange";
        }
    }

    @PostMapping("/admins/users/add")
    public String userAdd(@Valid User user,
                          Errors errors,
                          Model model,
                          @RequestParam String confirmPassword){

        if(userService.addUser(user,errors,model,confirmPassword)){
            return "redirect:/admins/users";
        } else{
            return "admins/userAdd";
        }
    }
}
