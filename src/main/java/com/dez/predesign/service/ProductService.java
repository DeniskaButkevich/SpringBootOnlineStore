package com.dez.predesign.service;

import com.dez.predesign.data.User;
import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class ProductService {

    public void setProduct(Product productGet, Product productSet) {
        productSet.setName(productGet.getName());
        productSet.setBrand(productGet.getBrand());
        productSet.setColor(productGet.getColor());
        productSet.setDescription(productGet.getDescription());
        productSet.setNewProduct(productGet.isNewProduct());
        productSet.setPrice(productGet.getPrice());
        productSet.setSale(productGet.getSale());
    }

    @Transactional
    public Iterable<Product> getViewedProducts(Integer user_id, UserRepo userRepo) {
        return userRepo.findById(user_id).get().getProducts();
    }

    @Transactional
    public boolean addViewedProduct(Integer user_id, Product product, UserRepo userRepo) {
            User user = userRepo.findById(user_id).get();
            Set<Product> set = user.getProducts();
            if (!set.contains(product)) {
                set.add(product);
                if (set.size() > 3) {
                    set.iterator().remove();
                }
                userRepo.save(user);
                return true;
            }
            return false;
    }
}
