package com.dez.predesign.controller.store;

import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CopmareController {
    @Autowired
    ProductRepo productRepo;


    @GetMapping("/compare-products")
    public String compareProducts(Model model, @CookieValue(name = "compare", required = false) String compare){

        if(compare != null && !compare.isEmpty()){
            String[] values = compare.split("\\|");
            List<Long> ids = new ArrayList<>();

            for (String str :values) {
                ids.add(Long.parseLong(str));
            }
            Iterable<Product> products = productRepo.findAllProductsByIds(ids);
            model.addAttribute("products", products);
        }

        return "compare-products";
    }
}
