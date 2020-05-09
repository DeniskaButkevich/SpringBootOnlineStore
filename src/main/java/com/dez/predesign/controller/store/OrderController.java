package com.dez.predesign.controller.store;

import com.dez.predesign.data.Order;
import com.dez.predesign.data.Payment;
import com.dez.predesign.data.User;
import com.dez.predesign.repository.OrderRepo;
import com.dez.predesign.service.OrderService;
import com.dez.predesign.util.OrderUtil;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@Controller
public class OrderController {

    private OrderService orderService;
    private OrderRepo orderRepo;

    public OrderController(OrderService orderService, OrderRepo orderRepo) {
        this.orderService = orderService;
        this.orderRepo = orderRepo;
    }

    @GetMapping("/checkout")
    public String checkout(@AuthenticationPrincipal User user,
                           @CookieValue(name = "cart", required = false) String cart,
                           @CookieValue(name = "sizes", required = false) String sizes,
                           @CookieValue(name = "colors", required = false) String colors,
                           Model model) {

        Map<Long, String> map_sizes = OrderUtil.getCookieSizes(sizes);
        model.addAttribute("sizes", map_sizes);
        Map<Long, String> map_colors = OrderUtil.getCookieColors(colors);
        model.addAttribute("colors", map_colors);

        if (cart != null && !cart.isEmpty()) {
            orderService.addProductsModel(cart, model);
            model.addAttribute("user", user);
            if(user != null){
                if (user.getPayment() != null) {
                    model.addAttribute("payment", user.getPayment());
                }
            }
        }
        return "checkout";
    }

    @PostMapping("/orderSuccessful")
    public String confirmOrder(@AuthenticationPrincipal User user_auth,
                               @CookieValue(name = "cart", required = false) String cart,
                               @CookieValue(name = "sizes", required = false) String sizes,
                               @CookieValue(name = "colors", required = false) String colors,
                               @Valid User user,
                               BindingResult bindingResult,
                               Model model,
                               @Valid Payment payment,
                               BindingResult paymentBindingResult,
                               HttpServletResponse response) {

        Map<Long, String> map_sizes = OrderUtil.getCookieSizes(sizes);
        model.addAttribute("sizes", map_sizes);
        Map<Long, String> map_colors = OrderUtil.getCookieColors(colors);
        model.addAttribute("colors", map_colors);

        if (orderService.checkError(bindingResult, paymentBindingResult, user, model, payment, cart)) {
            return "checkout";
        }
        orderService.userAdressCheck(user_auth,user);

        Order order = orderService.setFields(user_auth, cart);
        orderService.setParams(order , cart, map_sizes, map_colors);
        orderRepo.save(order);

        OrderUtil.cleanCookie(cart,response);
        OrderUtil.cleanCookie(sizes,response);
        OrderUtil.cleanCookie(colors,response);

        return "redirect:/orderSuccessful";
    }
}
