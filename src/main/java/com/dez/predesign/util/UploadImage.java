package com.dez.predesign.util;

import com.dez.predesign.data.Product;
import com.dez.predesign.data.catalog.Image;
import com.dez.predesign.repository.ImageRepo;
import com.dez.predesign.repository.ProductRepo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class UploadImage {

    public static void uploadImage(String id, MultipartFile file, String uploadPath, ProductRepo productRepo, ImageRepo imageRepo, String flag) throws IOException {

        Product product = productRepo.findById(Long.parseLong(id)).get();
        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + file.getOriginalFilename();

        file.transferTo(new File(uploadPath + resultFilename));

        if (flag.equals("general"))
            product.setFilename(resultFilename);
        if (flag.equals("hover"))
            product.setHoverFilename(resultFilename);
        if (flag.equals("sec"))
            imageRepo.save(new Image(resultFilename, product));

        productRepo.save(product);
    }
}