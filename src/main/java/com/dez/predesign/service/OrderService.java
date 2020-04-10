package com.dez.predesign.service;

import com.dez.predesign.data.CountAndPrice;
import com.dez.predesign.data.Order;
import com.dez.predesign.data.User;
import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.repository.ProductRepo;
import com.dez.predesign.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    ProductRepo productRepo;

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

}
