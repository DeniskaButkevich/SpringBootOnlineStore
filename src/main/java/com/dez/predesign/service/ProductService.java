package com.dez.predesign.service;

import com.dez.predesign.data.catalog.Product;
import org.springframework.stereotype.Service;

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
}
