package com.dez.predesign.controller.store;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AllGetController {

    @GetMapping("/checkout")
    public String checkout(){
        return "/checkout";
    }

    @GetMapping("/contact")
    public String contact(){
        return "/contact";
    }

    @GetMapping("/aboutus")
    public String aboutus(){
        return "/aboutus";
    }

    @GetMapping("/cart")
    public String cart(){
        return "/cart";
    }

    @GetMapping("/compare-products")
    public String compareProducts(){
        return "/compare-products";
    }

    @GetMapping("/blog")
    public String blog(){
        return "/blog";
    }

    @GetMapping("/portfolio-2")
    public String portfolioTwo(){
        return "/portfolio-2";
    }

    @GetMapping("/portfolio-masonry-2")
    public String portfolioMasonryTwo(){
        return "/portfolio-masonry-2";
    }

    @GetMapping("/single")
    public String single(){
        return "/single";
    }

    @GetMapping("/single-portfolio")
    public String singlePortfolio(){
        return "/single-portfolio";
    }

    @GetMapping("/single-portfolio-gallery")
    public String singlePortfolioGallery(){
        return "/single-portfolio-gallery";
    }

    @GetMapping("/single-portfolio-video")
    public String singlePortfolioVideo(){
        return "/single-portfolio-video";
    }
}
