package com.dez.predesign.controller;

import com.dez.predesign.data.User;
import com.dez.predesign.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/admins/users")
    public String usersShow(Model model){

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

        return "redirect:/admins/message";
    }
    @PostMapping("/admins/edit/{id}")
    public String messageEdit(@PathVariable String id, User user){
        User usr = userRepo.findById(Integer.parseInt(id)).get();

        usr.setUsername(user.getUsername());
        usr.setFirstName(user.getFirstName());
        usr.setLastName(user.getLastName());
        usr.setPostCode(user.getPostCode());
        usr.setPhoneNumber(user.getPhoneNumber());
        usr.setAddress(user.getAddress());
        usr.setPhoneNumber(user.getEmail());

        userRepo.save(usr);

        return "redirect:/admins/users";
    }
}
