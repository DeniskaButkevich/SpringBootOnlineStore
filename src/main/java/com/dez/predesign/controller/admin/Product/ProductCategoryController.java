package com.dez.predesign.controller.admin.Product;

import com.dez.predesign.data.catalog.Category;
import com.dez.predesign.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admins/product/categories")
public class ProductCategoryController {

    @Autowired
    CategoryRepo categoryRepo;

    @ModelAttribute(name = "categories")
    public List<Category> categories() {
        return categoryRepo.findByLevelAndDescendant(1, null);
    }

    @ModelAttribute(name = "setCategoriesLevelOne")
    public List<Category> setCategoriesLevelOne() {
        return categoryRepo.findByLevelAndDescendant(1, null);
    }

    @ModelAttribute(name = "setCategoriesLevelTwo")
    public List<Category> setCategoriesLevelTwo() {
        return categoryRepo.findByLevel(2);
    }

    @GetMapping
    public String showCategories(Model model) {
        return "/admins/productCategories";
    }

    @PostMapping("/level_1")
    public String levelOne(@RequestParam String name, Model model) {

        List<Category> categories = categoryRepo.findByLevel(1);

        if (categories.stream().anyMatch(s -> s.getName().equals(name))) {
            model.addAttribute("errorCategory", "Name already exist");
            return "/admins/productCategories";
        }
        categoryRepo.save(new Category(name, 1));

        return "redirect:/admins/product/categories";
    }

    @PostMapping("/level_2")
    public String levelTwo(@RequestParam String categoryLevelOne,
                           @RequestParam String categoryLevelTwo,
                           Model model) {
        Category levelOne = categoryRepo.findByNameAndLevelAndDescendant(categoryLevelOne, 1, null);

        List<Category> categories = categoryRepo.findByLevel(2);

        if (categories.stream().anyMatch(s -> s.getName().equals(categoryLevelTwo))) {
            if (categories.stream().anyMatch(s -> s.getAncestor().getId() == levelOne.getId())) {
                model.addAttribute("categoryLevelTwoError", "Name already exist");
                return "/admins/productCategories";
            }
        }

        Category levelTwo = new Category(categoryLevelTwo, 2, levelOne);
        categoryRepo.save(levelTwo);

        Category oneOgo = new Category(categoryLevelOne, levelTwo, 1);
        categoryRepo.save(oneOgo);


        return "redirect:/admins/product/categories";
    }

    @GetMapping("delete/{id}")
    public String deleteCategory(@PathVariable String id) {

        Category category = categoryRepo.findById(Long.parseLong(id)).get();

        Category ancestor = categoryRepo.findByDescendant(category);
        Iterable<Category> descendants = categoryRepo.findByAncestor(category);

        if (ancestor != null)
            categoryRepo.delete(ancestor);

        for (Category descendant : descendants) {
            Category ancestor_ = categoryRepo.findByDescendant(descendant);
            categoryRepo.delete(ancestor_);
            categoryRepo.delete(descendant);
        }

        categoryRepo.delete(category);

        return "redirect:/admins/product/categories";
    }

}
