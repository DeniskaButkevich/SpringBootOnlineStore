package com.dez.predesign.controller.admin.Product;

import com.dez.predesign.data.catalog.Image;
import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.repository.ImageRepo;
import com.dez.predesign.repository.ProductRepo;
import com.dez.predesign.util.UploadImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
public class ProductImagesController {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    ImageRepo imageRepo;

    @Value("${AWS_ACCESS_KEY_ID}")
    private String AWS_ACCESS_KEY_ID;

    @Value("${AWS_SECRET_ACCESS_KEY}")
    public String AWS_SECRET_ACCESS_KEY;

    @Value("${S3_BUCKET_NAME}")
    private String S3_BUCKET_NAME;

    @GetMapping("/admins/product/images/{id}")
    public String imagesShow(@PathVariable String id, Model model) {
        Product product = productRepo.findById(Long.parseLong(id)).get();
        model.addAttribute("product", product);
        return "admins/productImages";
    }

    @PostMapping("/admins/product/images/{id}")
    public String imagesAdd(@PathVariable String id, Model model,
                            @RequestParam("imageGeneral") MultipartFile general,
                            @RequestParam("imageHover") MultipartFile hover,
                            @RequestParam("imageSecondary") MultipartFile[] secondary) throws IOException {

        if (general != null && !general.getOriginalFilename().isEmpty()){
            UploadImage.uploadImage(id, general, productRepo, imageRepo, "general", AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, S3_BUCKET_NAME);
        }
        if (hover != null && !hover.getOriginalFilename().isEmpty()){
            UploadImage.uploadImage(id, hover, productRepo, imageRepo, "hover", AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, S3_BUCKET_NAME);
        }
        for (MultipartFile sec : secondary) {
            if (sec != null && !sec.getOriginalFilename().isEmpty())
                UploadImage.uploadImage(id, sec, productRepo, imageRepo, "sec", AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, S3_BUCKET_NAME);
        }
        return "redirect:/admins/product/images/{id}";
    }


    @GetMapping("/admins/product/images/{id}/delete/{filetype}")
    public String imagesDelete(@PathVariable String id, @PathVariable String filetype) throws IOException {

        Product product = productRepo.findById(Long.parseLong(id)).get();

        if (filetype.equals("general")) {
            UploadImage.deleteObjectAmazonS3(product.getFilename(), S3_BUCKET_NAME, AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
            product.setFilename(null);
            productRepo.save(product);

        } else if (filetype.equals("hover")) {
            UploadImage.deleteObjectAmazonS3(product.getHoverFilename(), S3_BUCKET_NAME, AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
            product.setHoverFilename(null);
            productRepo.save(product);
        } else {
            UploadImage.deleteObjectAmazonS3(filetype, S3_BUCKET_NAME, AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
            imageRepo.delete(imageRepo.findByName(filetype));
        }
        return "redirect:/admins/product/images/{id}";
    }

}
