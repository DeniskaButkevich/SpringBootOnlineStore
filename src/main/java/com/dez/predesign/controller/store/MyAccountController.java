package com.dez.predesign.controller.store;

import com.dez.predesign.data.User;
import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.repository.OrderRepo;
import com.dez.predesign.repository.ProductRepo;
import com.dez.predesign.repository.UserRepo;
import com.dez.predesign.service.OrderService;
import com.dez.predesign.service.ProductService;
import com.dez.predesign.util.ProductSelector;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MyAccountController {

    private ProductRepo productRepo;
    private UserRepo userRepo;
    private ProductService productService;
    private OrderRepo orderRepo;

    public MyAccountController(ProductRepo productRepo,
                               OrderRepo orderRepo,
                               UserRepo userRepo,
                               ProductService productService) {
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.productService = productService;
    }

    @GetMapping("/my-account")
    public String myAccount(@RequestParam(required = false, name = "menu_item") String menu_item,
                            @CookieValue(name = "wishlist", required = false) String wishlist,
                            Authentication authentication,
                            Model model) {

            switch (menu_item) {
                case "orders":
                    if (authentication != null) {
                        User user = (User) authentication.getPrincipal();
                        model.addAttribute("orders", orderRepo.findByUser(user));
                    }
                    break;
                case "viewed-products":
                    if (authentication != null) {
                        User user = (User) authentication.getPrincipal();
                        Integer user_id = user.getId();
                        model.addAttribute("viewedProducts", productService.getViewedProducts(user_id, userRepo));
                    }
                    break;
                case "CaA":
                    if (authentication != null) {
                        User user = (User) authentication.getPrincipal();
                        model.addAttribute("user", user);
                    }
                    break;
                case "wishlist":
                    Iterable<Product> products = ProductSelector.getViaCookie(wishlist, productRepo);
                    model.addAttribute("wishlist", products);
                    break;
                default:
            }
            return "my-account";
    }


}
