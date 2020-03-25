package com.dez.predesign.controller.store;

import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.data.catalog.Image;
import com.dez.predesign.repository.ImageRepo;
import com.dez.predesign.repository.ProductRepo;
import com.dez.predesign.util.ProductSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class ProductsController {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    ImageRepo imageRepo;

    @ModelAttribute(name = "featured_products")
    public Page<Product> featuredProduct(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 8) Pageable pageable){
        Page<Product> page =productRepo.findAll(pageable);
        return page;
    }

    @ModelAttribute(name = "also_purchased_products")
    public Iterable<Product> alsoPurchasedProduct(){
        Iterable<Product> products = ProductSelector.getTandomProducts(6, productRepo);
        return products;
    }

    @GetMapping("/product/{id}")
    public String show(@PathVariable String id, Model model){
        Product product = productRepo.findById(Long.parseLong(id)).get();
        Iterable<Image> images = imageRepo.findByProduct(product);

        model.addAttribute("product", product);
        model.addAttribute("images",images);

        return "product";
    }
}
