package com.dez.predesign.service;

import com.dez.predesign.data.CountAndPrice;
import com.dez.predesign.data.Order;
import com.dez.predesign.data.Payment;
import com.dez.predesign.data.User;
import com.dez.predesign.data.catalog.Color;
import com.dez.predesign.data.catalog.Params;
import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.data.catalog.Size;
import com.dez.predesign.repository.*;
import com.dez.predesign.util.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    SizeRepo sizeRepo;

    @Autowired
    ColorRepo colorRepo;

    @Autowired
    ParamsRepo paramsRepo;

    public Map<Product, List<Double>> addProductsModel(String cart, Model model) {
        Map<Product, List<Double>> products = new HashMap<>();
        Double total_price = 0D;

        String[] values = cart.split("\\|");
        for (String str : values) {
            String[] param = str.split("-");

            Product product = productRepo.findOneProduct(Long.parseLong(param[0]));

            Double quantity_price = Double.parseDouble(param[1]) * product.getPrice();
            total_price += quantity_price;

            ArrayList<Double> count_and_price = new ArrayList<>();
            count_and_price.add(Double.parseDouble(param[1]));
            count_and_price.add(quantity_price);

            products.put(product, count_and_price);
        }

        model.addAttribute("products", products);
        model.addAttribute("total_price", total_price);

        return products;
    }

    public Set<Product> getProductByCookie(String cart) {
        Set<Product> products = new HashSet<>();
        String[] values = cart.split("\\|");

        for (String str : values) {
            String[] param = str.split("-");
            Product product = productRepo.findOneProduct(Long.parseLong(param[0]));
            products.add(product);

        }
        return products;
    }

    public Map<Product, CountAndPrice> setCountPrice(String cart) {
        Map<Product, CountAndPrice> countAndPrice = new HashMap<>();
        Double total_price = 0D;

        String[] values = cart.split("\\|");
        for (String str : values) {
            String[] param = str.split("-");

            Product product = productRepo.findOneProduct(Long.parseLong(param[0]));
            Double quantity_price = Double.parseDouble(param[1]) * product.getPrice();
            total_price += quantity_price;

            CountAndPrice count_and_price = new CountAndPrice();
            count_and_price.setCount(Integer.parseInt(param[1]));
            count_and_price.setPrice(quantity_price);

            countAndPrice.put(product, count_and_price);
        }
        return countAndPrice;
    }

    public Double setTotalPrice(String cart) {
        Double total_price = 0D;
        String[] values = cart.split("\\|");
        for (String str : values) {
            String[] param = str.split("-");
            Product product = productRepo.findOneProduct(Long.parseLong(param[0]));
            total_price += Double.parseDouble(param[1]) * product.getPrice();
            ;
        }
        return total_price;
    }

    @Transactional
    public Iterable<Order> getOrders(Integer user_id, UserRepo userRepo) {
        return userRepo.findById(user_id).get().getOrders();
    }

    public void userAdressCheck(User user_auth, User user) {
        if(user_auth != null){
            user_auth.setAddress(user.getAddress());
            user_auth.setPostCode(user.getPostCode());
            userRepo.save(user_auth);
        }
    }

    public Order setFields( User user_auth, String cart) {
        Order order = new Order();
        order.setProducts(getProductByCookie(cart));
        order.setUser(user_auth);
        order.setCount_price(setCountPrice(cart));
        order.setTotal_price(setTotalPrice(cart));
        order.setActive(1);
        return order;
    }

    public void setParams(Order order, String cart, Map<Long, String> map_sizes, Map<Long, String> map_colors) {
        List<Params> params = new ArrayList<>();
        Set<Product> products = getProductByCookie(cart);
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
    }

    public boolean checkError(BindingResult bindingResult, BindingResult paymentBindingResult, User user, Model model, Payment payment, String cart) {
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
            addProductsModel(cart, model);

            return true;
        }
        return false;
    }
}
