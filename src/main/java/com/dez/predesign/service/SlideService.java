package com.dez.predesign.service;

import com.dez.predesign.data.Slide;
import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.repository.SlideRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.dez.predesign.util.UploadImage.deleteObjectAmazonS3;
import static com.dez.predesign.util.UploadImage.putObjectAmazonS3;

@Service
public class SlideService {
    @Value("${AWS_SECRET_ACCESS_KEY}")
    public String AWS_SECRET_ACCESS_KEY;
    @Autowired
    SlideRepo slideRepo;
    @Value("${AWS_ACCESS_KEY_ID}")
    private String AWS_ACCESS_KEY_ID;
    @Value("${S3_BUCKET_NAME}")
    private String S3_BUCKET_NAME;

    public void addSlide(Product product, MultipartFile file) throws IOException {
        Slide slide = new Slide();
        slide.setProduct(product);

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            String result_filename = putObjectAmazonS3(file, S3_BUCKET_NAME, AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
            slide.setFilename(result_filename);
            slideRepo.save(slide);
        }

        slideRepo.save(slide);
    }

    public void updateSlide(Integer id, Product product, MultipartFile file) throws IOException {
        if (product != null || (file != null && !file.getOriginalFilename().isEmpty())) {
            Slide slide = slideRepo.findById(id);
            if(product != null){
                slide.setProduct(product);
            }
            if (file != null && !file.getOriginalFilename().isEmpty()) {
                if(slide.getFilename() != null && !slide.getFilename().isEmpty()){
                    deleteObjectAmazonS3(slide.getFilename(), S3_BUCKET_NAME, AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
                }
                String result_filename = putObjectAmazonS3(file, S3_BUCKET_NAME, AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
                slide.setFilename(result_filename);
            }
            slideRepo.save(slide);
        }
    }

    public void deleteSlide(Integer id) {
        Slide slide = slideRepo.findById(id);
        if(slide != null){
            if(slide.getFilename() != null && !slide.getFilename().isEmpty()){
                deleteObjectAmazonS3(slide.getFilename(), S3_BUCKET_NAME, AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
            }
            slideRepo.delete(slide);
        }
    }
}
