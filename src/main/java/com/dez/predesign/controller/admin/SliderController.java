package com.dez.predesign.controller.admin;

import com.dez.predesign.data.SliderElement;
import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.repository.ImageRepo;
import com.dez.predesign.repository.ProductRepo;
import com.dez.predesign.repository.SliderElementRepo;
import com.dez.predesign.util.UploadImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class SliderController {

    @Autowired
    SliderElementRepo sliderElementRepo;

    @Autowired
    ProductRepo productRepo;

    @Value("${AWS_ACCESS_KEY_ID}")
    private String AWS_ACCESS_KEY_ID;

    @Value("${AWS_SECRET_ACCESS_KEY}")
    public String AWS_SECRET_ACCESS_KEY;

    @Value("${S3_BUCKET_NAME}")
    private String S3_BUCKET_NAME;

    @ModelAttribute(name = "ids")
    public List<Long> allProductId(){ return productRepo.findAllId();}

    @GetMapping("/admins/slider")
    public String slider(Model model){

        SliderElement slider_one = sliderElementRepo.findByNumber(1);
        SliderElement slider_two = sliderElementRepo.findByNumber(2);
        SliderElement slider_three = sliderElementRepo.findByNumber(3);
        model.addAttribute("slider_one", slider_one);
        model.addAttribute("slider_two", slider_two);
        model.addAttribute("slider_three", slider_three);

        return "admins/slider";
    }

    @PostMapping("/admins/slider/delete/{level_number}")
    public String sliderElementDelete(@PathVariable Integer level_number){

        SliderElement sliderElement = sliderElementRepo.findByNumber(level_number);
        String filename = sliderElement.getFilename();
        if(filename != null){
            UploadImage.deleteObjectAmazonS3(filename,S3_BUCKET_NAME, AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
        }
        sliderElementRepo.delete(sliderElement);

        return "redirect:/admins/slider";
    }

    @PostMapping("/admins/slider/add/{level_number}")
    public String sliderElementAdd(@PathVariable Integer level_number, @RequestParam Long product_id){

        sliderElementRepo.save(new SliderElement(
                level_number,
                productRepo.findById(product_id).get(),
                level_number
        ));

        return "redirect:/admins/slider";
    }

    @PostMapping("/admins/slider/imageAdd/{level_number}")
    public String sliderImgAdd(@PathVariable Integer level_number,
                            @RequestParam("file") MultipartFile file) throws IOException {
        if(file != null && !file.getOriginalFilename().isEmpty()){
            UploadImage.uploadImageSlider(level_number, sliderElementRepo, file, AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, S3_BUCKET_NAME);
        }

        return "redirect:/admins/slider";
    }

    @PostMapping("/admins/slider/imageDelete/{level_number}")
    public String sliderImgDelete(@PathVariable Integer level_number)  {

        SliderElement sliderElement = sliderElementRepo.findByNumber(level_number);
        String filename = sliderElement.getFilename();
        UploadImage.deleteObjectAmazonS3(filename,S3_BUCKET_NAME, AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
        sliderElement.setFilename(null);

        return "redirect:/admins/slider";
    }
}
