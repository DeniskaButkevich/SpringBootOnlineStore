package com.dez.predesign.controller.store;

import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.repository.ProductRepo;
import com.dez.predesign.util.OrderUtil;
import com.dez.predesign.util.ProductSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {

    @Autowired
    ProductRepo productRepo;

    @ModelAttribute(name = "also_purchased_products")
    public Iterable<Product> alsoPurchasedProduct(){
        Iterable<Product> products = ProductSelector.getRandomProducts(6, productRepo);
        return products;
    }

    @GetMapping("/cart")
    public String cart(Model model,
                       @CookieValue(name = "cart", required = false) String cart,
                       @CookieValue(name = "sizes", required = false) String sizes,
                       @CookieValue(name = "colors", required = false) String colors) {

        Iterable<Product> products = OrderUtil.getCookieProducts(cart, productRepo);
        model.addAttribute("products", products);

        Map<Long, String> map_sizes = OrderUtil.getCookieSizes(sizes);
        model.addAttribute("sizes", map_sizes);

        Map<Long, String> map_colors = OrderUtil.getCookieColors(colors);
        model.addAttribute("colors", map_colors);

        return "cart";
    }
}
