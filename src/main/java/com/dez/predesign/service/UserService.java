package com.dez.predesign.service;

import com.dez.predesign.data.Payment;
import com.dez.predesign.data.Role;
import com.dez.predesign.data.User;
import com.dez.predesign.repository.UserRepo;
import com.dez.predesign.util.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private MailSender mailSender;

    @Value("${hostname}")
    private String hostname;

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

    public boolean passwordChange(String id, String password, String confirmPassword, Model model) {
        if (password == null || password.isEmpty()){
            model.addAttribute("passwordEmpty", "Password is not the same");
            return false;
        }
        if(!password.equals(confirmPassword)){
            model.addAttribute("errorConform", "Password is not the same");
            return false;
        }
        User usr = userRepo.findById(Integer.parseInt(id)).get();
        usr.setPassword(passwordEncoder.encode(password));
        userRepo.save(usr);
        return true;
    }

    public boolean addUser(User user, BindingResult bindingResult, Model model, String confirmPassword) {
        if (userRepo.findByUsername(user.getUsername()) != null) {
            model.addAttribute("userExist", "This name is already in use.");
            return false;
        }
        if (!confirmPassword.equals(user.getPassword())) {
            model.addAttribute("errorConfirm", "passwords are not the same");
            return false;
        }
        if (bindingResult.hasErrors()){
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            errorsMap.put("hasErrors", "true");
            model.mergeAttributes(errorsMap);
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return true;
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        user.setActive(true);

        userRepo.save(user);

        return true;
    }

    public void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Sweater. Please, visit next link: http://%s/%s/activate",
                    user.getUsername(),
                    hostname,
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean RegistUser(User user, Errors errors, String confirmPassword) {

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);
        sendMessage(user);

        return true;
    }

    public boolean updatePayment(Integer id, Model model, Payment payment, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            errorsMap.put("paymentHasErrors", "true");
            model.mergeAttributes(errorsMap);
            return false;
        }
        User user = userRepo.findById(id).get();
        user.setPayment(payment);
        userRepo.save(user);
        return  true;
    }

    public boolean update(Integer id, Model model, User user, BindingResult bindingResult) {
        if (bindingResult.getErrorCount() > 1){
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            errorsMap.put("hasErrors", "true");
            model.mergeAttributes(errorsMap);
            return false;
        }
        User usr = userRepo.findById(id).get();
        usr.setUsername(user.getUsername());
        usr.setFirstName(user.getFirstName());
        usr.setLastName(user.getLastName());
        usr.setPostCode(user.getPostCode());
        usr.setPhoneNumber(user.getPhoneNumber());
        usr.setAddress(user.getAddress());
        usr.setEmail(user.getEmail());
        usr.setActive(user.isActive());
        usr.setRoles(user.getRoles());

        userRepo.save(usr);

        return true;
    }
}
