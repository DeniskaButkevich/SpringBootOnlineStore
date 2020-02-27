package com.dez.predesign.controller;

import com.dez.predesign.data.Product;
import com.dez.predesign.data.catalog.Brand;
import com.dez.predesign.data.catalog.Category;
import com.dez.predesign.data.catalog.Image;
import com.dez.predesign.repository.BrandRepo;
import com.dez.predesign.repository.CategoryRepo;
import com.dez.predesign.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    BrandRepo brandRepo;

    @ModelAttribute(name = "brands")
    public Iterable<Brand> brands() { return brandRepo.findAll();}

    @ModelAttribute(name = "productsOnSale")
    public Iterable<Product> productsOnSale() {return productRepo.findBySaleNotNull();}

    @ModelAttribute(name = "categoriesLevelOne")
    public List<Category> setCategoriesLevelOne() {
        return categoryRepo.findByLevelAndDescendant(1, null);
    }

    @ModelAttribute(name = "categoriesLevelTwo")
    public List<Category> setCategoriesLevelTwo() {
        return categoryRepo.findByLevel(2);
    }

    @ModelAttribute

    @GetMapping(value = {"/index","/",""})
    public String show(Model model, @RequestParam(required = false) String brand){

        Iterable<Product> products = null;
        if(brand != null && !brand.isEmpty()){
            products = productRepo.findByBrand(brand);
        }else
            products = productRepo.findAll();

        List<Product> newProducts = (List<Product>) productRepo.findByNewProductNotNullAndImageListNotNull();
        Image one = newProducts.get(2).getImageList().get(0);
        Image two = newProducts.get(2).getImageList().get(1);
        Image qq = newProducts.get(2).getImageList().get(2);
        model.addAttribute("newProducts",newProducts);
        return "/index";
    }
}
