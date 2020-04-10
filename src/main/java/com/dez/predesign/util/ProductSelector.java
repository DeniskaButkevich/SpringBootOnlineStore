package com.dez.predesign.util;

import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.repository.ProductRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductSelector {

    public static Iterable<Product> getRandomProducts(Integer count, ProductRepo productRepo) {
        List<Long> lds = productRepo.findAllId();
        List<Long> random_ids = new ArrayList<>();

        for(int i = 0; i < 6; i++){
            Random rand = new Random();
            Long randomElement = lds.get(rand.nextInt(lds.size()));
            random_ids.add(randomElement);
        }
        Iterable<Product> products = productRepo.findAllProductsByIds(random_ids);
        return products;
    }


    public static Iterable<Product> getViaCookie(String cookie, ProductRepo productRepo) {
        if (cookie != null && !cookie.isEmpty()) {
            String[] values = cookie.split("\\|");
            List<Long> ids = new ArrayList<>();

            for (String str : values) {
                ids.add(Long.parseLong(str));
            }
            return productRepo.findAllProductsByIds(ids);
        }
        return null;
    }
}
