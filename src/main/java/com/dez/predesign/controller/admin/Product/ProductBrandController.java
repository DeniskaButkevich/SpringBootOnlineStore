package com.dez.predesign.controller.admin.Product;

import com.dez.predesign.data.catalog.Brand;
import com.dez.predesign.repository.BrandRepo;
import com.dez.predesign.util.UploadImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admins/product/brands")
public class ProductBrandController {

    @Autowired
    BrandRepo brandRepo;

    @Value("${AWS_ACCESS_KEY_ID}")
    private String AWS_ACCESS_KEY_ID;

    @Value("${AWS_SECRET_ACCESS_KEY}")
    public String AWS_SECRET_ACCESS_KEY;

    @Value("${S3_BUCKET_NAME}")
    private String S3_BUCKET_NAME;

    @ModelAttribute(name = "brands")
    public Iterable<Brand> brands() {
        return brandRepo.findAll();
    }

    @GetMapping
    public String showBrands(Model model) { return "admins/productBrands"; }

    @PostMapping("add")
    public String addBrands(@RequestParam String name, Model model, @RequestParam("file") MultipartFile file) throws IOException {

        Iterable<Brand> brands = brandRepo.findByName(name);

        for (Brand b : brands) {
            if (b.getName().equals(name)) {
                model.addAttribute("nameError", "brand already exist");

                return "admins/productBrands";
            }
        }

        Brand brand = new Brand(name);
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            String resultFilenameTest = UploadImage.putObjectAmazonS3(file, S3_BUCKET_NAME, AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
            brand.setFilename(resultFilenameTest);
        }
        brandRepo.save(brand);

        return "redirect:/admins/product/brands";
    }

    @GetMapping("delete/{id}")
    public String deleteBrands(@PathVariable String id) {

        brandRepo.deleteById(id);

        return "redirect:/admins/product/brands";
    }

}
