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

            if(user.getPayment() != null)
                model.addAttribute("payment", user.getPayment());

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
                               BindingResult paymentBindingResult){

        if(bindingResult.getErrorCount() > 2 || paymentBindingResult.hasErrors()){
            System.out.println(bindingResult.getErrorCount());
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            if(user.getAddress() == null || user.getAddress().isEmpty())
                errorsMap.put("addressError", "address is required");
            if(user.getPostCode() == null || user.getPostCode().isEmpty())
                errorsMap.put("postCodeError","postCode is required");

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
        orderRepo.save(order);

        return "redirect:/orderSuccessful";
    }
}
