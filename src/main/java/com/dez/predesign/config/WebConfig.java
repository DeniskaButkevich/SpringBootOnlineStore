package com.dez.predesign.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
        registry.addViewController("/aboutus.html").setViewName("aboutus");
        registry.addViewController("/blog.html").setViewName("blog");
        registry.addViewController("/cart.html").setViewName("cart");
        registry.addViewController("/category.html").setViewName("category");
        registry.addViewController("/category-list.html").setViewName("category-list");
        registry.addViewController("/compare-products.html").setViewName("compare-products");
        registry.addViewController("/contacts.html").setViewName("contacts");
        registry.addViewController("/footer1.html").setViewName("footer1");
        registry.addViewController("/header1.html").setViewName("header1");
        registry.addViewController("/login.html").setViewName("");
        registry.addViewController("/portfolio-2.html").setViewName("portfolio-2");
        registry.addViewController("/portfolio-masonry-2.html").setViewName("portfolio-masonry-2");
        registry.addViewController("/product.html").setViewName("product");
        registry.addViewController("/register-account.html").setViewName("register-account");
        registry.addViewController("/single.html").setViewName("single");
        registry.addViewController("/single-portfolio.html").setViewName("single-portfolio");
        registry.addViewController("/single-portfolio-gallery.html").setViewName("single-portfolio-gallery");
        registry.addViewController("/single-portfolio-video.html").setViewName("single-portfolio-video");
        registry.addViewController("/checkout.html").setViewName("checkout");
    }
}
