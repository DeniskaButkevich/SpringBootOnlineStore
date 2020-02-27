package com.dez.predesign.controller;

import com.dez.predesign.data.Product;
import com.dez.predesign.data.catalog.Brand;
import com.dez.predesign.data.catalog.Category;
import com.dez.predesign.data.catalog.Color;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
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
                       @RequestParam Map<String, String> allRequestParams,
                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 10) Pageable pageable) {

        //Sort by
        if (allRequestParams.get("filter") != null && !allRequestParams.get("filter").isEmpty())
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(
                    allRequestParams.get("filter")
            ));

        Category categoryFilter = null;
        Brand brandFilter = null;

        Page<Product> page = null;
        Iterable<Category> filterCategory = categoryRepo.findByLevel(2);
        Iterable<Brand> filterBrand = brandRepo.findAll();

        if(allRequestParams.get("brand") != null && !allRequestParams.get("brand").isEmpty())
            brandFilter = brandRepo.findByName(allRequestParams.get("brand"));

        if (allRequestParams.get("category") != null && !allRequestParams.get("category").isEmpty()) {

            Category category = categoryRepo.findById(
                    Long.parseLong(
                            allRequestParams.get("category"))).get();

            if (category.getLevel() == 2) {
                filterCategory = categoryRepo.findByAncestor(category.getAncestor());
            } else {
                filterCategory = categoryRepo.findByAncestor(category);
            }
            categoryFilter = category;
        }

        Product productForExample = Product.builder().category(categoryFilter).brand(brandFilter).build();

        ExampleMatcher matcher =
                ExampleMatcher.matching().withIncludeNullValues().withIgnorePaths("id", "color", "price", "description", "filename", "hoverFilename", "sale", "newProduct")
                        .withNullHandler(ExampleMatcher.NullHandler.IGNORE);

        Example<Product> example = Example.of(productForExample, matcher);

        page = productRepo.findAll(example, pageable);

        model.addAttribute("page", page);
        model.addAttribute("filterCategory", filterCategory);
        model.addAttribute("filterBrand", filterBrand);
        List<Integer> listpages = pageService.listPages(pageable, page);
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

        return "/category";
    }
}
