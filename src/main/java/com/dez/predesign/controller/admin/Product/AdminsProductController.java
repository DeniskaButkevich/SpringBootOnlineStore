package com.dez.predesign.controller.admin.Product;

import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.repository.ProductRepo;
import com.dez.predesign.service.ProductService;
import com.dez.predesign.util.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class AdminsProductController {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    ProductService productService;

    @GetMapping("/admins/product/{id}")
    public String getOneProductForAdmin(@PathVariable String id, Model model){
        model.addAttribute("product", productRepo.findOneProduct(Long.parseLong(id)));
        return "admins/product";
    }

    @PostMapping("/admins/product/{id}")
    public String productAdd(@PathVariable String id, @Valid Product product, BindingResult bindingResult, Model model)  {
        String return_url;

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            errorsMap.put("hasErrors", "true");
            model.mergeAttributes(errorsMap);
            return_url = "admins/product";
        } else {
            Product old_product = productRepo.findById(Long.parseLong(id)).get();
            productService.update(old_product, product);
            productRepo.save(old_product);
            return_url = "redirect:/admins/product/" + id;
        }
        model.addAttribute("product", productRepo.findById(Long.parseLong(id)).get());
        return return_url;
    }
}
