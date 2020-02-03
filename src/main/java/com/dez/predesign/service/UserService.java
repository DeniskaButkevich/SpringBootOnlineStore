package com.dez.predesign.service;

import com.dez.predesign.data.Role;
import com.dez.predesign.data.User;
import com.dez.predesign.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user != null) {
            return user;
        }
        throw new UsernameNotFoundException(
                "User '" + username + "' not found");
    }

    public boolean save(String id, Map<String, String> form, User user, Model model, Errors errors) {

        User usr = userRepo.findById(Integer.parseInt(id)).get();

        if(userRepo.findByUsername(user.getUsername()) != null ){
            if(user.getId() != userRepo.findByUsername(user.getUsername()).getId()) {
                model.addAttribute("userExist", "This name is already in use.");
                return false;
            }
        }

        if(errors.hasErrors() ){
            if(errors.getErrorCount()>1)
                return  false;
        }

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

        for (String key : form.keySet())
            if (roles.contains(key))
                usr.getRoles().add(Role.valueOf(key));

        userRepo.save(usr);

        return true;
    }

    public boolean passwordChange(String id, String password, String confirmPassword, Model model) {
        User usr = userRepo.findById(Integer.parseInt(id)).get();

        if (password == null || password.isEmpty()){
            model.addAttribute("passwordEmpty", "Password is not the same");
            return false;
        }
        if(!password.equals(confirmPassword)){
            model.addAttribute("errorConform", "Password is not the same");
            return false;
        }
        usr.setPassword(passwordEncoder.encode(password));
        userRepo.save(usr);
        return true;
    }

    public void addUser(User user, Errors errors, Model model, String confirmPassword) {
    }
}
