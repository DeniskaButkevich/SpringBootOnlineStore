package com.dez.predesign.controller.admin;

import com.dez.predesign.data.Slide;
import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.repository.ProductRepo;
import com.dez.predesign.repository.SlideRepo;
import com.dez.predesign.service.SlideService;
import com.dez.predesign.util.UploadImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/admins/slider")
public class SliderController {

    @Autowired
    SlideRepo slideRepo;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    SlideService slideService;

    @ModelAttribute(name = "ids")
    public List<Long> allProductId(){ return productRepo.findAllId();}
    @ModelAttribute(name = "slides")
    public Iterable<Slide> getSlides(){ return slideRepo.findAll();}

    @GetMapping()
    public String slider(){
        return "admins/slider";
    }

    @PostMapping("/add")
    public String slideAdd(@RequestParam Product product, @RequestParam("file") MultipartFile file) throws IOException {
        slideService.addSlide(product, file);
        return "redirect:/admins/slider";
    }

    @PostMapping("/update")
    public String slideUpdate(@RequestParam Product product, @RequestParam("file") MultipartFile file, @RequestParam Integer id) throws IOException {
        slideService.updateSlide(id,product,file);
        return "redirect:/admins/slider";
    }

    @PostMapping("/delete")
    public String slideDelete(@RequestParam Integer id){
        slideService.deleteSlide(id);
        return "redirect:/admins/slider";
    }
}
