package com.dez.predesign.controller;

import com.dez.predesign.data.catalog.Category;
import com.dez.predesign.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class IndexController {

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

    @GetMapping(value = {"/index","/"})
    public String show(Model model){

        return "/index";
    }
}
