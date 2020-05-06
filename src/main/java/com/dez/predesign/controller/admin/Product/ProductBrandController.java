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

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate JdbcTemplate;

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
    public String addBrands(@RequestParam String name, Model model, @RequestParam("file") MultipartFile file, @RequestParam String but) throws IOException {

        if(name.isEmpty()){
            model.addAttribute("nameError", "name is empty");
            return "admins/productBrands";
        }

        if(but.equals("Update")){
            Iterable<Brand> brands = brandRepo.findByName(name);
            if(!brands.iterator().hasNext()){
                model.addAttribute("nameError", "no such brand");
                return "admins/productBrands";
            }else{
                Brand brand = brands.iterator().next();
                if (file != null && !file.getOriginalFilename().isEmpty()) {
                    if(!brand.getFilename().isEmpty()){
                        UploadImage.deleteObjectAmazonS3( brand.getFilename() ,S3_BUCKET_NAME, AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
                    }
                    String resultFilenameTest = UploadImage.putObjectAmazonS3(file, S3_BUCKET_NAME, AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
                    brand.setFilename(resultFilenameTest);
                }
            }

        }else if(but.equals("Add brand")){
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
        }

        return "redirect:/admins/product/brands";
    }

    @GetMapping("delete/{id}")
    public String deleteBrands(@PathVariable String id) {
        Brand brand = brandRepo.findById(id).get();
        if (brand.getFilename() != null && !brand.getFilename().isEmpty()) {
            UploadImage.deleteObjectAmazonS3(brand.getFilename() ,S3_BUCKET_NAME, AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);

        }
        JdbcTemplate.update("UPDATE product SET brand_name = NULL WHERE brand_name = ?;", id);
        brandRepo.delete(brand);

        return "redirect:/admins/product/brands";
    }

}
