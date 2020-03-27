package com.dez.predesign.controller.store;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class AllGetController {

    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }

    @GetMapping("/aboutus")
    public String aboutus(){
        return "aboutus";
    }

    @GetMapping("/blog")
    public String blog(){
        return "blog";
    }

    @GetMapping("/portfolio-masonry-2")
    public String portfolioMasonryTwo(){
        return "portfolio-masonry-2";
    }

    @GetMapping("/single")
    public String single(){
        return "single";
    }

    @GetMapping("/single-portfolio")
    public String singlePortfolio(){
        return "single-portfolio";
    }

    @GetMapping("/single-portfolio-gallery")
    public String singlePortfolioGallery(){
        return "single-portfolio-gallery";
    }

    @GetMapping("/orderSuccessful")
    public String orderSuccessful(){
        return "orderSuccessful";
    }
}
