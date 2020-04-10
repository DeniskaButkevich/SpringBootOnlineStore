package com.dez.predesign.controller.store;

import com.dez.predesign.data.User;
import com.dez.predesign.data.catalog.Image;
import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.repository.ImageRepo;
import com.dez.predesign.repository.ProductRepo;
import com.dez.predesign.repository.UserRepo;
import com.dez.predesign.service.ProductService;
import com.dez.predesign.util.ProductSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductsController {

    private ProductRepo productRepo;
    private ImageRepo imageRepo;
    private ProductService productService;

    @Autowired
    private UserRepo userRepo;

    public ProductsController(ProductRepo productRepo,
                              ImageRepo imageRepo,
                              ProductService productService) {
        this.productRepo = productRepo;
        this.imageRepo = imageRepo;
        this.productService = productService;
    }

    @ModelAttribute(name = "featured_products")
    public Page<Product> featuredProduct(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 8) Pageable pageable) {
        Page<Product> page = productRepo.findAll(pageable);
        return page;
    }

    @ModelAttribute(name = "also_purchased_products")
    public Iterable<Product> alsoPurchasedProduct() {
        Iterable<Product> products = ProductSelector.getRandomProducts(6, productRepo);
        return products;
    }


    @GetMapping("/product/{id}")
    public String show(@PathVariable String id, Model model, Authentication authentication) {
        Product product = productRepo.findById(Long.parseLong(id)).get();
        Iterable<Image> images = imageRepo.findByProduct(product);

        model.addAttribute("product", product);
        model.addAttribute("images", images);

        if (authentication != null) {
            User user = (User) authentication.getPrincipal();
            Integer authentication_user_id = user.getId();
            productService.addViewedProduct(authentication_user_id, product, userRepo);
        }

        return "product";
    }
}
