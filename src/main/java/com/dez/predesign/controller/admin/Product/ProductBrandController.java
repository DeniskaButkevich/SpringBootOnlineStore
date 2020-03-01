package com.dez.predesign.controller.admin.Product;

import com.dez.predesign.data.catalog.Brand;
import com.dez.predesign.repository.BrandRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admins/product/brands")
public class ProductBrandController {

    @Autowired
    BrandRepo brandRepo;

    @ModelAttribute(name = "brands")
    public Iterable<Brand> brands() {
        return brandRepo.findAll();
    }

    @GetMapping
    public String showBrands(Model model){
        return "/admins/productBrands";
    }

    @PostMapping("add")
    public String addBrands(@RequestParam String name,Model model){

       Iterable<Brand> brand = brandRepo.findByName(name);

       if(brand != null) {
           model.addAttribute("nameError", "brand already exist");
           return "/admins/productBrands";
       }

       brandRepo.save(new Brand(name));

       return "redirect:/admins/product/brands";
    }

    @GetMapping("delete/{id}")
    public String deleteBrands(@PathVariable String id){

        brandRepo.deleteById(id);

        return "redirect:/admins/product/brands";
    }

}
