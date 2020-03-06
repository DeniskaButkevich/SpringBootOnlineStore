package com.dez.predesign.controller.store;

import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.data.catalog.Image;
import com.dez.predesign.repository.ImageRepo;
import com.dez.predesign.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductsController {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    ImageRepo imageRepo;

    @GetMapping("/product/{id}")
    public String show(@PathVariable String id, Model model){
        Product product = productRepo.findById(Long.parseLong(id)).get();
        Iterable<Image> images = imageRepo.findByProduct(product);

        model.addAttribute("product", product);
        model.addAttribute("images",images);

        return "product";
    }
}
