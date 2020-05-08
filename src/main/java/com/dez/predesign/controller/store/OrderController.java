package com.dez.predesign.controller.store;

import com.dez.predesign.data.Order;
import com.dez.predesign.data.Payment;
import com.dez.predesign.data.User;
import com.dez.predesign.data.catalog.Color;
import com.dez.predesign.data.catalog.Params;
import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.data.catalog.Size;
import com.dez.predesign.repository.*;
import com.dez.predesign.service.OrderService;
import com.dez.predesign.util.ControllerUtils;
import com.dez.predesign.util.OrderUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    SizeRepo sizeRepo;

    @Autowired
    ColorRepo colorRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    ParamsRepo paramsRepo;

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

        if (bindingResult.getErrorCount() > 2 || paymentBindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            if (user.getAddress() == null || user.getAddress().isEmpty()) {
                errorsMap.put("addressError", "address is required");
            }
            if (user.getPostCode() == null || user.getPostCode().isEmpty()) {
                errorsMap.put("postCodeError", "postCode is required");
            }

            Map<String, String> errorsMapPayment = ControllerUtils.getErrors(paymentBindingResult);
            model.mergeAttributes(errorsMapPayment);

            model.mergeAttributes(errorsMap);
            model.addAttribute("payment", payment);
            orderService.addProductsModel(cart, model);

            return "checkout";
        }

        Order order = new Order();
        order.setProducts(orderService.getProductByCookie(cart));
        order.setUser(user_auth);
        order.setCount_price(orderService.setCountPrice(cart));
        order.setTotal_price(orderService.setTotalPrice(cart));

        List<Params> params = new ArrayList<>();
        Set<Product> products = orderService.getProductByCookie(cart);


        for(Product prod :products){
            Long id = prod.getId();
            Size size = sizeRepo.findBySize(map_sizes.get(id));
            Color color = colorRepo.findByRgb(map_colors.get(id));
            Product product = productRepo.findById(id).get();

            Params param = new Params(product,size,color);
            paramsRepo.save(param);
            params.add(param);
        }
        order.setParams(params);

        orderRepo.save(order);

        Cookie cookie = new Cookie("cart", null); // Not necessary, but saves bandwidth.
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // Don't set to -1 or it will become a session cookie!
        response.addCookie(cookie);

        Cookie cookie_size = new Cookie("sizes", null); // Not necessary, but saves bandwidth.
        cookie_size.setPath("/");
        cookie_size.setHttpOnly(true);
        cookie_size.setMaxAge(0); // Don't set to -1 or it will become a session cookie!
        response.addCookie(cookie_size);

        Cookie cookie_colors = new Cookie("colors", null); // Not necessary, but saves bandwidth.
        cookie_colors.setPath("/");
        cookie_colors.setHttpOnly(true);
        cookie_colors.setMaxAge(0); // Don't set to -1 or it will become a session cookie!
        response.addCookie(cookie_colors);

        return "redirect:/orderSuccessful";
    }
}
