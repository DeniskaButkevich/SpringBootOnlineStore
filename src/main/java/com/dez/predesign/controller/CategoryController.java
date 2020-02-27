package com.dez.predesign.controller;

import com.dez.predesign.data.Product;
import com.dez.predesign.data.catalog.Category;
import com.dez.predesign.repository.CategoryRepo;
import com.dez.predesign.repository.ProductRepo;
import com.dez.predesign.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.naming.Name;
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

        if (allRequestParams.get("filter") != null && !allRequestParams.get("filter").isEmpty())
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(
                    allRequestParams.get("filter")
            ));

        Page<Product> page = null;
        Iterable<Category> filterCategory = filterCategory = categoryRepo.findByLevel(2);

        if(allRequestParams.get("category") != null && !allRequestParams.get("category").isEmpty()){
            Category category = categoryRepo.findById(
                    Long.parseLong(
                            allRequestParams.get("category"))).get();

            if (category.getLevel() == 2) {
                page = productRepo.findByCategory(category, pageable);
                filterCategory = categoryRepo.findByAncestor(category.getAncestor());
            } else {
                page = productRepo.findByCategory_Ancestor(category, pageable);
                filterCategory = categoryRepo.findByAncestor(category);
            }
        }else
            page = productRepo.findAll(pageable);

        model.addAttribute("page", page);
        model.addAttribute("filterCategory", filterCategory);

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
