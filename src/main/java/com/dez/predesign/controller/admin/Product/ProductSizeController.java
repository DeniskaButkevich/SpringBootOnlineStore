package com.dez.predesign.controller.admin.Product;

import com.dez.predesign.data.catalog.Size;
import com.dez.predesign.repository.SizeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admins/product/sizes")
public class ProductSizeController {

    @Autowired
    SizeRepo sizeRepo;

    @ModelAttribute(name = "sizes")
    public Iterable<Size> sizes() {
        return sizeRepo.findAll();
    }

    @GetMapping
    public String showSizes(Model model){
        return "admins/productSizes";
    }

    @PostMapping("delete")
    public String deleteSize(@RequestParam Size color){
        sizeRepo.delete(color);
        return "redirect:/admins/product/sizes";
    }

    @PostMapping("add")
    public String addSize(Model model, @RequestParam String size){

        if(size.isEmpty() || size == null){
            model.addAttribute("sizeError","size must be not null");
            return "admins/productSizes";
        }

        if(sizeRepo.findBySize(size) != null){
            model.addAttribute("sizeError","size already exist");
            return "admins/productSizes";
        }
        Size new_size = new Size(size);

        sizeRepo.save(new_size);
        return "redirect:/admins/product/sizes";
    }
}
