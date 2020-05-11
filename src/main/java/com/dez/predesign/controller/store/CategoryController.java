package com.dez.predesign.controller.store;

import com.dez.predesign.data.catalog.Brand;
import com.dez.predesign.data.catalog.Category;
import com.dez.predesign.data.catalog.Color;
import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.repository.*;
import com.dez.predesign.service.CategoryService;
import com.dez.predesign.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Controller
public class CategoryController {

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    BrandRepo brandRepo;

    @Autowired
    ColorRepo colorRepo;

    @Autowired
    PageService pageService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    EntityManager entityManager;

    @Autowired
    ProductRepoImpl productRepoImpl;

    @ModelAttribute(name = "featured_products")
    public Page<Product> featuredProduct(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 8) Pageable pageable){
        Page<Product> page =productRepo.findAll(pageable);
        return page;
    }

    @ModelAttribute(name = "brands")
    public Iterable<Brand> getBrands() {return brandRepo.findAll();}

    @ModelAttribute(name = "colors")
    public Iterable<Color> colors() {
        return colorRepo.findAll();
    }

    @ModelAttribute(name = "categories")
    public Iterable<Category> getCategories() {return categoryRepo.findByLevel(1);}

    @ModelAttribute(name = "categories_des")
    public Iterable<Category> getCategoriesDes() {return categoryRepo.findByLevel(2);}


    @GetMapping("/category")
    public String show(Model model,
                       @RequestParam Map<String, String> allRequestParams,
                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        //Sort by
        pageable = categoryService.sortByFilter(pageable, allRequestParams);
        //Add products in model for category page
        List<Product> products = productRepoImpl.getData(allRequestParams,entityManager);
        Page<Product> page = new PageImpl<>(products, pageable, products.size());
        model.addAttribute("page", page);
        //add Count Page For pagination
        List<Integer> listpages = pageService.listPages(page);
        model.addAttribute("listpages", listpages);
        //merge request param for build filtration on page
        model.mergeAttributes(allRequestParams);

        //start filter by param ////////////////////////////
        Iterable<Category> categoriesForFilter = null;
        boolean p_category_exist = allRequestParams.get("category") != null && !allRequestParams.get("category").isEmpty();
        boolean p_brand_exist = allRequestParams.get("brand") != null && !allRequestParams.get("brand").isEmpty();
        //category for filter
        if (p_category_exist) {
            Category category = categoryRepo.findById(
                    Long.parseLong(
                            allRequestParams.get("category"))).get();
            if (category.getLevel() == 2){
                categoriesForFilter = categoryRepo.findByAncestor(category.getAncestor());
            } else{
                categoriesForFilter = categoryRepo.findByAncestor(category);
            }
        } else{
            categoriesForFilter = categoryRepo.findByLevel(2);
        }
        //end filter by param ///////////////////////////////////////
        model.addAttribute("filterCategory", categoriesForFilter);

        //Set url and param
        String setParam = categoryService.setUrlParams(allRequestParams);
        model.addAttribute("setParam", setParam);
        model.addAttribute("url", "/category");
        model.addAttribute("text",allRequestParams);
        //Add one top prodact
        Product productForTop = categoryService.getProduct();
        model.addAttribute("productForTop", productForTop);

        ////////////////////////////////////////////////////////////////////////
        return "category";
    }
}
