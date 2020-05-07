package com.dez.predesign.controller.admin;

import com.dez.predesign.data.Payment;
import com.dez.predesign.data.Role;
import com.dez.predesign.data.User;
import com.dez.predesign.repository.UserRepo;
import com.dez.predesign.service.PageService;
import com.dez.predesign.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(value = "/admins/users")
public class UserController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PageService pageService;


    @ModelAttribute(name = "roles")
    public Role[] roles() {
        return Role.values();
    }

    @GetMapping()
    public String usersShow(Model model, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 5) Pageable pageable) {
        Page<User> page = userRepo.findAll(pageable);
        List<Integer> listpages = pageService.listPages(page);
        model.addAttribute("listpages", listpages);
        model.addAttribute("page", page);
        return "admins/userList";
    }

    @PostMapping()
    public String userAdd(@Valid User user,
                          BindingResult bindingResult,
                          Model model,
                          @RequestParam String confirmPassword,
                          @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 5) Pageable pageable) {

        if (userService.addUser(user, bindingResult, model, confirmPassword)) {
            return "redirect:/admins/users";
        } else {
            Page<User> page = userRepo.findAll(pageable);
            model.addAttribute("page", page);
            return "admins/userList";
        }
    }

    @GetMapping("/delete")
    public String userRemove(@RequestParam String id) {
        userRepo.delete(userRepo.findById(Integer.parseInt(id)).get());
        return "redirect:/admins/users";
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable Integer id, Model model) {

        User user = userRepo.findById(id).get();
        model.addAttribute("user", user);
        return "admins/userOne";
    }

    @PostMapping("/{id}")
    public String userUpdate(@PathVariable Integer id,
                             Model model,
                             @Valid User user,
                             BindingResult bindingResult) {
      if(userService.update(id, model, user, bindingResult)){
          return "redirect:/admins/users/" + id;
      }
        return "admins/userOne";
    }

    @PostMapping("/password/{id}")
    public String userPasswordChange(@RequestParam String confirmPassword, @PathVariable String id, @RequestParam String password, Model model) {
        if (userService.passwordChange(id, password, confirmPassword, model)) {
            return "redirect:/admins/users";
        } else {
            return "admins/passwordChange";
        }
    }

    @PostMapping("/{id}/payment/update")
    public String paymentUpdate(@PathVariable Integer id,
                                Model model,
                                @Valid Payment payment,
                                BindingResult bindingResult) {
        if(userService.updatePayment(id, model, payment, bindingResult)){
            return "redirect:/admins/users" + id;
        }else {
            return "admins/userOne";
        }
    }
}
