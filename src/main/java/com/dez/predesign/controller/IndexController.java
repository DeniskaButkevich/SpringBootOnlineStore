package com.dez.predesign.controller;

import com.dez.predesign.data.Product;
import com.dez.predesign.data.catalog.Brand;
import com.dez.predesign.data.catalog.Category;
import com.dez.predesign.repository.BrandRepo;
import com.dez.predesign.repository.CategoryRepo;
import com.dez.predesign.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    BrandRepo brandRepo;

    @ModelAttribute(name = "categoriesLevelOne")
    public List<Category> setCategoriesLevelOne() {
        return categoryRepo.findByLevelAndDescendant(1, null);
    }

    @ModelAttribute(name = "categoriesLevelTwo")
    public List<Category> setCategoriesLevelTwo() {
        return categoryRepo.findByLevel(2);
    }

    @GetMapping(value = {"/index","/",""})
    public String show(Model model,
                       @PageableDefault(size = 6,direction = Sort.Direction.DESC, sort = {"id"})Pageable pageable){

        HashMap<String,Page<Product>> mapProducts;
        mapProducts = new HashMap<>();

        Page<Product> products = null;
        products = productRepo.findAll(pageable);
        mapProducts.put("productsAll",products);

        Page<Product> productsOnSale = productRepo.findBySaleNotNull(pageable);
        mapProducts.put("productsOnSale",productsOnSale);

        Page<Product> newProducts = productRepo.findByNewProductNotNullAndImageListNotNull(pageable);
        mapProducts.put("newProducts",newProducts);

        Iterable<Brand> brands = brandRepo.findAll();
        model.addAttribute("brands",brands);

        for (Brand brand : brands) {
            products = productRepo.findByBrand(brand, pageable);
            mapProducts.put(brand.getName(),products);
        }

        model.addAttribute("mapProducts",mapProducts);
        return "/index";
    }
}
