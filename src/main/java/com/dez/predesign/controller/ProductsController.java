package com.dez.predesign.controller;

import com.dez.predesign.data.Product;
import com.dez.predesign.data.catalog.Category;
import com.dez.predesign.data.catalog.Image;
import com.dez.predesign.repository.CategoryRepo;
import com.dez.predesign.repository.ImageRepo;
import com.dez.predesign.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductsController {

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    ImageRepo imageRepo;

    @ModelAttribute(name = "categoriesLevelOne")
    public List<Category> setCategoriesLevelOne() {
        return categoryRepo.findByLevelAndDescendant(1, null);
    }

    @ModelAttribute(name = "categoriesLevelTwo")
    public List<Category> setCategoriesLevelTwo() {
        return categoryRepo.findByLevel(2);
    }

    @GetMapping("/product/{id}")
    public String show(@PathVariable String id, Model model){
        Product product = productRepo.findById(Long.parseLong(id)).get();
        Iterable<Image> images = imageRepo.findAll();

        model.addAttribute("product", product);
        model.addAttribute("images",images);

        return "product";
    }
}
