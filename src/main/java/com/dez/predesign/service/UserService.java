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

    public void save(String id, Map<String, String> form, User user) {
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
        usr.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(usr);
    }


}
