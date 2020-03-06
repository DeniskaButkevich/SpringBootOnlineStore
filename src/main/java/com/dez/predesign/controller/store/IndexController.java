package com.dez.predesign.controller.store;

import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.data.catalog.Brand;
import com.dez.predesign.repository.BrandRepo;
import com.dez.predesign.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;

@Controller
public class IndexController {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    BrandRepo brandRepo;

    @GetMapping(value = {"/index","/",""})
    public String show(Model model,
                       @PageableDefault(size = 6,direction = Sort.Direction.DESC, sort = {"id"})Pageable pageable){

        HashMap<String,Page<Product>> mapProducts;
        mapProducts = new HashMap<>();

        Page<Product> products = null;
        products = productRepo.findAll(pageable);
        mapProducts.put("productsAll",products);

        Page<Product> productsOnSale = productRepo.findBySaleNotNull(pageable);
        mapProducts.put("productsOnSale",productsOnSale);

        Page<Product> newProducts = productRepo.findByNewProductNotNullAndImageListNotNull(pageable);
        mapProducts.put("newProducts",newProducts);

        Iterable<Brand> brands = brandRepo.findAll();
        model.addAttribute("brands",brands);

        for (Brand brand : brands) {
            products = productRepo.findByBrand(pageable, brand);
            mapProducts.put(brand.getName(),products);
        }

        model.addAttribute("mapProducts",mapProducts);
        return "/index";
    }
}
