package com.dez.predesign.controller;

import com.dez.predesign.data.Product;
import com.dez.predesign.data.catalog.Image;
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

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Controller
public class ProductImagesController {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    ImageRepo imageRepo;

    @Value("${upload.path.catalog}")
    private String uploadPath;

    @GetMapping("/admins/product/images/{id}")
    public String imagesShow(@PathVariable String id, Model model) {
        Product product = productRepo.findById(Long.parseLong(id)).get();
        Iterable<Image> images = imageRepo.findByProduct(product);

        model.addAttribute("images", images);
        model.addAttribute("product", product);

        return "/admins/productImages";
    }

    @PostMapping("/admins/product/images/{id}")
    public String imagesAdd(@PathVariable String id, Model model,
                            @RequestParam("imageGeneral") MultipartFile general,
                            @RequestParam("imageHover") MultipartFile hover,
                            @RequestParam("imageSecondary") MultipartFile[] secondary) throws IOException {


        if (general != null && !general.getOriginalFilename().isEmpty())
            UploadImage.uploadImage(id, general, uploadPath, productRepo, imageRepo, "general");

        if (hover != null && !hover.getOriginalFilename().isEmpty())
            UploadImage.uploadImage(id, hover, uploadPath, productRepo, imageRepo, "hover");

        for (MultipartFile sec : secondary) {
            if (sec != null && !sec.getOriginalFilename().isEmpty())
                UploadImage.uploadImage(id, sec, uploadPath + "secondary\\", productRepo, imageRepo, "sec");
        }

        return "redirect:/admins/product/images/{id}";
    }


    @GetMapping("/admins/product/images/{id}/delete/{filetype}")
    public String imagesDelete(@PathVariable String id, @PathVariable String filetype) throws IOException {

        Product product = productRepo.findById(Long.parseLong(id)).get();

        if (filetype.equals("general")) {
            Files.delete(Paths.get(uploadPath + product.getFilename()));
            product.setFilename(null);
            productRepo.save(product);

        } else if (filetype.equals("hover")) {
            Files.delete(Paths.get(uploadPath + product.getHoverFilename()));
            product.setHoverFilename(null);
            productRepo.save(product);
        } else {
            Files.delete(Paths.get(uploadPath + "secondary\\" + filetype));
            imageRepo.delete(imageRepo.findByName(filetype));
        }
        return "redirect:/admins/product/images/{id}";
    }

}
