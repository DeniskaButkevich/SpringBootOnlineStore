package com.dez.predesign.controller.store;

import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.data.catalog.Brand;
import com.dez.predesign.data.catalog.Category;
import com.dez.predesign.repository.BrandRepo;
import com.dez.predesign.repository.CategoryRepo;
import com.dez.predesign.repository.ColorRepo;
import com.dez.predesign.repository.ProductRepo;
import com.dez.predesign.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/category")
    public String show(Model model,
                       @RequestParam Map<String, String> allRequestParams,
                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        //Sort by
        if (allRequestParams.get("filter") != null && !allRequestParams.get("filter").isEmpty())
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(
                    allRequestParams.get("filter")
            ));

        //start filter by param
        Iterable<Category> categoriesForFilter = null;

        boolean p_category_exist = allRequestParams.get("category") != null && !allRequestParams.get("category").isEmpty();
        boolean p_brand_exist = allRequestParams.get("brand") != null && !allRequestParams.get("brand").isEmpty();

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

        Iterable<Brand> brandForFilter = null;
        if(p_brand_exist)
            brandForFilter = brandRepo.findByName(allRequestParams.get("brand"));
        else
            brandForFilter = brandRepo.findAll();
        //end filter by param

        Page<Product> page = null;

        if(p_category_exist && p_brand_exist){
            page = productRepo.findBC(pageable, categoriesForFilter, brandForFilter);
        }else if(p_brand_exist){
            page = productRepo.findB(pageable, brandForFilter);
        }else if(p_category_exist){
            page = productRepo.findC(pageable,categoriesForFilter);
        }else
            page = productRepo.findAll(pageable);


        brandForFilter = brandRepo.findAll();

        model.addAttribute("page", page);
        model.addAttribute("filterCategory", categoriesForFilter);
        model.addAttribute("filterBrand", brandForFilter);

        List<Integer> listpages = pageService.listPages(page);
        model.addAttribute("listpages", listpages);

        String setParam = "";
        String url = "/category";
        if (allRequestParams.size() != 0) {
            Set<String> keySet = allRequestParams.keySet();
            for (String key : keySet) {
                String param = allRequestParams.get(key);
                setParam = setParam + key + "=" + param + "&";
            }
        }
        model.addAttribute("url", url);
        model.addAttribute("setParam", setParam);

        List<Long> lds = productRepo.findAllId();

        Random rand = new Random();
        Long randomElement = lds.get(rand.nextInt(lds.size()));

        Product productForTop = productRepo.findOneProduct(randomElement);
        model.addAttribute("productForTop", productForTop);
        return "/category";
    }
}
