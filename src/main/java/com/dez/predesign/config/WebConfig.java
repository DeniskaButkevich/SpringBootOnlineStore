package com.dez.predesign.config;

import com.dez.predesign.util.RedirectInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:" + uploadPath);
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RedirectInterceptor());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/checkout.html").setViewName("checkout");
        registry.addViewController("/contact.html").setViewName("contact");
        registry.addViewController("/aboutus.html").setViewName("aboutus");
        registry.addViewController("/cart.html").setViewName("cart");
        registry.addViewController("/compare-products.html").setViewName("compare-products");

        registry.addViewController("/blog.html").setViewName("blog");
        registry.addViewController("/portfolio-2.html").setViewName("portfolio-2");
        registry.addViewController("/portfolio-masonry-2.html").setViewName("portfolio-masonry-2");
        registry.addViewController("/single.html").setViewName("single");
        registry.addViewController("/single-portfolio.html").setViewName("single-portfolio");
        registry.addViewController("/single-portfolio-gallery.html").setViewName("single-portfolio-gallery");
        registry.addViewController("/single-portfolio-video.html").setViewName("single-portfolio-video");

        registry.addViewController("/admins/indexAdmin").setViewName("/admins/indexAdmin");
        registry.addViewController("/404.html").setViewName("404");
        registry.addViewController("/404").setViewName("404");
        registry.addViewController("/403.html").setViewName("403");
        registry.addViewController("/403").setViewName("403");
    }
}
