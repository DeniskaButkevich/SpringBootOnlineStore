package com.dez.predesign.util;

import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.repository.ProductRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderUtil {
    public static Map<Long, String> getCookieSizes(String sizes) {
        Map<Long, String> map_sizes = new HashMap<>();
        if(sizes != null && !sizes.isEmpty()){
            String[] values = sizes.split("\\|");
            for (String str :values) {
                String[] params = str.split("-");
                map_sizes.put(Long.parseLong(params[0]), params[1]);
            }
        }
        return map_sizes;
    }

    public static Map<Long, String> getCookieColors(String colors) {
        Map<Long, String> map_colors = new HashMap<>();
        if(colors != null && !colors.isEmpty()){
            String[] values = colors.split("\\|");
            for (String str :values) {
                String[] params = str.split("-");
                String[] rgb_arr = params[1].split("_");
                String rgb = rgb_arr[0] + ", " + rgb_arr[1] + ", " + rgb_arr[2];
                map_colors.put(Long.parseLong(params[0]), rgb);
            }
        }
        return  map_colors;
    }

    public static Iterable<Product> getCookieProducts(String cart, ProductRepo productRepo) {
        Iterable<Product> products = null;
        if(cart != null && !cart.isEmpty()){
            String[] values = cart.split("\\|");
            List<Long> ids = new ArrayList<>();

            for (String str :values) {
                String[] id = str.split("-");
                ids.add(Long.parseLong(id[0]));
            }
           products = productRepo.findAllProductsByIds(ids);
        }
        return products;
    }
}
