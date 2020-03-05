package com.dez.predesign.controller.store;

import com.dez.predesign.data.Product;
import com.dez.predesign.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CheckoutController {

    @Autowired
    ProductRepo productRepo;

    @GetMapping("/checkout")
    public String checkout(Model model, @CookieValue(name = "cart", required = false) String cart) {

        if(cart != null && !cart.isEmpty()){
            Map<Product, List<Double>> products = new HashMap<>();

            String[] values = cart.split("\\|");
            Double total_price = 0D;

            for (String str :values) {
                String[] param = str.split("-");

                Product product = productRepo.findOneProduct(Long.parseLong(param[0]));

                Double quantity_price = Double.parseDouble(param[1]) * product.getPrice();
                total_price += quantity_price;

                ArrayList<Double> count_and_price = new ArrayList<>();
                count_and_price.add(Double.parseDouble(param[1]));
                count_and_price.add(total_price);

                products.put(product, count_and_price);
            }

            model.addAttribute("products", products);
            model.addAttribute("total_price", total_price);
            return "/checkout";
        }
        return "/checkout";
    }

    @PostMapping("/orderSuccessful")
    public String confirmOrder(){

        return "/orderSuccessful";
    }

}
