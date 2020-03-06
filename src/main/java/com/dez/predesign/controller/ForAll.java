package com.dez.predesign.controller;

import com.dez.predesign.data.User;
import com.dez.predesign.data.catalog.Category;
import com.dez.predesign.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice(basePackages = "com.dez.predesign.controller.store")
public class ForAll {

    @Autowired
    CategoryRepo categoryRepo;

    @ModelAttribute(name = "categoriesLevelOne")
    public List<Category> setCategoriesLevelOne() {
        return categoryRepo.findByLevelAndDescendant(1, null);
    }

    @ModelAttribute(name = "categoriesLevelTwo")
    public List<Category> setCategoriesLevelTwo() {
        return categoryRepo.findByLevel(2);
    }

    @ModelAttribute(name = "authorizedUserId")
    public Integer getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId;

        if (authentication.getPrincipal().toString().equals("anonymousUser")) {
            userId = -1;
        } else {
            User customUser = (User) authentication.getPrincipal();
            userId = customUser.getId();
        }
        return userId;
    }
}
