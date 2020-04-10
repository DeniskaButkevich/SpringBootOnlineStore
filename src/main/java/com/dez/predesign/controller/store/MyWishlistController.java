package com.dez.predesign.controller.store;

import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.repository.ProductRepo;
import com.dez.predesign.util.ProductSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MyWishlistController {

    @Autowired
    ProductRepo productRepo;

    @ModelAttribute(name = "also_purchased_products")
    public Iterable<Product> alsoPurchasedProduct(){
        Iterable<Product> products = ProductSelector.getRandomProducts(6, productRepo);
        return products;
    }

    @GetMapping("/my-wishlist")
    public String myWishlist(Model model, @CookieValue(name = "wishlist", required = false) String wishlist){
        Iterable<Product> products = ProductSelector.getViaCookie(wishlist, productRepo);
        model.addAttribute("products", products);
        return "my-wishlist";
    }
}
