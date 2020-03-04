package com.dez.predesign.controller.store;

import com.dez.predesign.data.Product;
import com.dez.predesign.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CheckoutController {

    @Autowired
    ProductRepo productRepo;

    @GetMapping("/checkout")
    public String checkout(Model model, @CookieValue(name = "cart", required = false) String cart) {

        if(cart != null && !cart.isEmpty()){

            Map<Product,String> products = new HashMap<>();

            String[] values = cart.split("\\|");
            Double total_price = 0D;

            for (String str :values) {
                String[] param = str.split("-");

                Product product = productRepo.findOneProduct(Long.parseLong(param[0]));
                products.put(product, param[1]);

                total_price += Double.parseDouble(param[1]) * product.getPrice();
            }

            model.addAttribute("products", products);
            model.addAttribute("total_price", total_price);
            return "/checkout";
        }
        return "/checkout";
    }
}
