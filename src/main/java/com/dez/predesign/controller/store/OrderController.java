package com.dez.predesign.controller.store;

import com.dez.predesign.data.Order;
import com.dez.predesign.data.Payment;
import com.dez.predesign.data.User;
import com.dez.predesign.repository.OrderRepo;
import com.dez.predesign.service.OrderService;
import com.dez.predesign.util.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepo orderRepo;

    @GetMapping("/checkout")
    public String checkout(@AuthenticationPrincipal User user,
                           @CookieValue(name = "cart", required = false) String cart,
                           Model model){

        if(cart != null && !cart.isEmpty()){
            orderService.addProductsModel(cart,model);
            model.addAttribute("user",user);

            if(user.getPayment() != null){
                model.addAttribute("payment", user.getPayment());
            }
            return "/checkout";
        }
        return "/checkout";
    }

    @PostMapping("/orderSuccessful")
    public String confirmOrder(@AuthenticationPrincipal User user_auth,
                               @CookieValue(name = "cart", required = false) String cart,
                               @Valid User user,
                               BindingResult bindingResult,
                               Model model,
                               @Valid Payment payment,
                               BindingResult paymentBindingResult,
                               HttpServletResponse response){

        if(bindingResult.getErrorCount() > 2 || paymentBindingResult.hasErrors()){
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            if(user.getAddress() == null || user.getAddress().isEmpty()){
                errorsMap.put("addressError", "address is required");
            }
            if(user.getPostCode() == null || user.getPostCode().isEmpty()){
                errorsMap.put("postCodeError","postCode is required");
            }

            Map<String, String> errorsMapPayment = ControllerUtils.getErrors(paymentBindingResult);
            model.mergeAttributes(errorsMapPayment);

            model.mergeAttributes(errorsMap);
            model.addAttribute("payment", payment);
            orderService.addProductsModel(cart,model);

            return "/checkout";
        }
        Order order = new Order();
        order.setProducts(orderService.getProductByCookie(cart));
        order.setUser(user_auth);
        order.setCount_price(orderService.setCountPrice(cart));
        order.setTotal_price(orderService.setTotalPrice(cart));
        orderRepo.save(order);

        Cookie cookie = new Cookie("cart", null); // Not necessary, but saves bandwidth.
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // Don't set to -1 or it will become a session cookie!
        response.addCookie(cookie);

        return "redirect:/orderSuccessful";
    }
}
