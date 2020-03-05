package com.dez.predesign.controller.store;

import com.dez.predesign.data.Product;
import com.dez.predesign.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    ProductRepo productRepo;

    @GetMapping("/cart")
    public String cart(Model model, @CookieValue(name = "cart", required = false) String cart) {

        if(cart != null && !cart.isEmpty()){
            String[] values = cart.split("\\|");
            List<Long> ids = new ArrayList<>();

            for (String str :values) {
                String[] id = str.split("-");
                ids.add(Long.parseLong(id[0]));
            }
            Iterable<Product> products = productRepo.findAllProductsByIds(ids);
            model.addAttribute("products", products);
            return "/cart";
        }
        return "/cart";
    }
}