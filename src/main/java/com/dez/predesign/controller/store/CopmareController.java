package com.dez.predesign.controller.store;

import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.repository.ProductRepo;
import com.dez.predesign.util.ProductSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CopmareController {
    @Autowired
    ProductRepo productRepo;


    @GetMapping("/compare-products")
    public String compareProducts(Model model, @CookieValue(name = "compare", required = false) String compare) {

        Iterable<Product> products = ProductSelector.getViaCookie(compare, productRepo);
        model.addAttribute("products", products);

        return "compare-products";
    }
}
