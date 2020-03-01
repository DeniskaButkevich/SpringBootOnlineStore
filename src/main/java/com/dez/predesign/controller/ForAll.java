package com.dez.predesign.controller;

import com.dez.predesign.data.catalog.Category;
import com.dez.predesign.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
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
}
