package com.dez.predesign.controller;

import com.dez.predesign.data.Product;
import com.dez.predesign.data.catalog.Category;
import com.dez.predesign.repository.CategoryRepo;
import com.dez.predesign.repository.ProductRepo;
import com.dez.predesign.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    PageService pageService;

    @ModelAttribute(name = "categoriesLevelOne")
    public List<Category> setCategoriesLevelOne() {
        return categoryRepo.findByLevelAndDescendant(1, null);
    }

    @ModelAttribute(name = "categoriesLevelTwo")
    public List<Category> setCategoriesLevelTwo() {
        return categoryRepo.findByLevel(2);
    }

    @GetMapping("/category")
    public String show(Model model,
                       @RequestParam(required = false) String category_id,
                       @PageableDefault(sort = {"id"},direction = Sort.Direction.DESC, size = 10) Pageable pageable){

        Page<Product> page = null;


        if(category_id != null){
            Category category = categoryRepo.findById(Long.parseLong(category_id)).get();
            if(category.getLevel() == 2){
                page = productRepo.findByCategory(category, pageable);
            }else{
                page = productRepo.findByCategory_Ancestor(category,pageable);
            }
        }else
            page = productRepo.findAll(pageable);

        List<Integer> listpages = pageService.listPages(pageable, page);

        model.addAttribute("listpages", listpages);
        model.addAttribute("page", page);
        model.addAttribute("url", "/category");

        return "/category";
    }
}
