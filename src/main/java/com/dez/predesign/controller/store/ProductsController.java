package com.dez.predesign.controller.store;

import com.dez.predesign.data.Message;
import com.dez.predesign.data.User;
import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.repository.MessageRepo;
import com.dez.predesign.repository.ProductRepo;
import com.dez.predesign.repository.UserRepo;
import com.dez.predesign.service.ProductService;
import com.dez.predesign.util.ControllerUtils;
import com.dez.predesign.util.ProductSelector;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ProductsController {

    private ProductRepo productRepo;
    private ProductService productService;
    private MessageRepo messageRepo;

    @Autowired
    private UserRepo userRepo;

    public ProductsController(ProductRepo productRepo,
                              ProductService productService,
                              MessageRepo messageRepo) {
        this.productRepo = productRepo;
        this.productService = productService;
        this.messageRepo= messageRepo;
    }

    @ModelAttribute(name = "featured_products")
    public Page<Product> featuredProduct(@PageableDefault(direction = Sort.Direction.DESC, size = 8) Pageable pageable) {
        Page<Product> page = productRepo.findAll(pageable);
        return page;
    }

    @ModelAttribute(name = "also_purchased_products")
    public Iterable<Product> alsoPurchasedProduct() {
        Iterable<Product> products = ProductSelector.getRandomProducts(6, productRepo);
        return products;
    }

    @GetMapping("/product/{id}")
    public String show(@PathVariable Long id,
                       Model model,
                       Authentication authentication,
                       @PageableDefault(sort = {"placedAt"},direction = Sort.Direction.DESC,size = 3) Pageable pageable) {
        return addAttributeModel(id, model, authentication, pageable);
    }

    @PostMapping("/product/{id}")
    public String addRating(@PathVariable Long id,
                            @Valid Message message,
                            BindingResult bindingResult,
                            Model model,
                            Authentication authentication,
                            @PageableDefault(sort = {"placedAt"},direction = Sort.Direction.DESC, size = 3) Pageable pageable){
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            return addAttributeModel(id, model, authentication, pageable);
        }
        if (authentication != null) {
            User user = (User) authentication.getPrincipal();
            message.setAuthor(user);
        }
        Product product = productRepo.findById(id).get();
        message.setProduct(product);
        messageRepo.save(message);

        return "redirect:/product/" + id;
    }

    @NotNull
    public String addAttributeModel(@PathVariable Long id,
                                    Model model,
                                    Authentication authentication,
                                    Pageable pageable) {
        Product product = productRepo.findById(id).get();
        model.addAttribute("product", product);

        Integer rating = productService.getRating(product);
        model.addAttribute("rating", rating);

        if (authentication != null) {
            User user = (User) authentication.getPrincipal();
            Integer authentication_user_id = user.getId();
            productService.addViewedProduct(authentication_user_id, product, userRepo);
        }

        Page<Message> page = messageRepo.findAll(pageable);
        model.addAttribute("page", page);

        List<Integer> list_pages = new ArrayList<>();
        for (int i = 1; i <= page.getTotalPages(); i++)
            list_pages.add(i);

        model.addAttribute("list_pages", list_pages);

        return  "product";
    }
}
