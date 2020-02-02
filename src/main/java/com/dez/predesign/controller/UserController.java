package com.dez.predesign.controller;

import com.dez.predesign.data.Role;
import com.dez.predesign.data.User;
import com.dez.predesign.repository.UserRepo;
import com.dez.predesign.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserService userService;

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
    public String usersShow(@ModelAttribute User user){
        return "admins/userList";
    }

    @GetMapping("/admins/users/edit/{id}")
    public String userEditShow(@PathVariable String id,Model model){

        User usr = userRepo.findById(Integer.parseInt(id)).get();
        model.addAttribute("user",usr);

        return "admins/userEdit";
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

        if(userRepo.findByUsername(user.getUsername()) != null ){
            if(user.getId() != userRepo.findByUsername(user.getUsername()).getId()) {
                model.addAttribute("userExist", "This name is already in use.");
                return "/admins/userEdit";
            }
        }
        if(errors.hasErrors())
            return  "/admins/userEdit";

        userService.save(id,form,user);

        return "redirect:/admins/users";
    }
    @PostMapping("/admins/users/add")
    public String addUser(@Valid User user, Errors errors, Model model){

        if(userRepo.findByUsername(user.getUsername()) != null){
            model.addAttribute("userExist", "This name is already in use.");
            return "/admins/userAdd";
        }
        if(errors.hasErrors())
            return "/admins/userAdd";

        user.setActive(true);

        userRepo.save(user);

        return "redirect:/admins/users";
    }

}
