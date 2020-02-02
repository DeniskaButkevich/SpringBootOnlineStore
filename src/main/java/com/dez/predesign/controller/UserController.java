package com.dez.predesign.controller;

import com.dez.predesign.data.Role;
import com.dez.predesign.data.User;
import com.dez.predesign.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @ModelAttribute(name="users")
    public Iterable<User> users() {
        return userRepo.findAll();
    }

    @ModelAttribute(name="roles")
    public Role[] roles(){
        return Role.values();
    }

    @GetMapping("/admins/users")
    public String usersShow(Model model, @ModelAttribute User user){

        Iterable<User> users = userRepo.findAll();
        model.addAttribute("users", users);

        return "admins/userList";
    }

    @GetMapping("/admins/delete")
    public String userRemove(@RequestParam String id) {
        userRepo.delete(
                userRepo.findById(
                        Integer.parseInt(id)).get()
        );

        return "redirect:/admins/users";
    }

    @PostMapping("/admins/edit/{id}")
    public String userEdit(
            @RequestParam Map<String, String> form,
            @PathVariable String id,
            User user,
            Model model){

        User usr = userRepo.findById(Integer.parseInt(id)).get();

        usr.setUsername(user.getUsername());
        usr.setFirstName(user.getFirstName());
        usr.setLastName(user.getLastName());
        usr.setPostCode(user.getPostCode());
        usr.setPhoneNumber(user.getPhoneNumber());
        usr.setAddress(user.getAddress());
        usr.setEmail(user.getEmail());

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        usr.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                usr.getRoles().add(Role.valueOf(key));
            }
        }

        userRepo.save(usr);

        return "redirect:/admins/users";
    }
    @PostMapping("/admins/users/add")
    public String addUser(@Valid User user, Errors errors, Model model){

        if(errors.hasErrors()){
            return "/admins/userList";
        }

        if(userRepo.findByUsername(user.getUsername()) != null){
            return "redirect:/admins/users";
        }
        user.setActive(true);

        userRepo.save(user);


        return "redirect:/admins/users";
    }
}
