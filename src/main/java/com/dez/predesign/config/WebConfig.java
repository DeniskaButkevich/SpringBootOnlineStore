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

    @Value("${upload.path.amazons3}")
    private String uploadPathAmazonS3;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations(uploadPathAmazonS3);
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RedirectInterceptor());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/admins/indexAdmin").setViewName("admins/indexAdmin");
        registry.addViewController("/404.html").setViewName("404");
        registry.addViewController("/404").setViewName("404");
        registry.addViewController("/403.html").setViewName("403");
        registry.addViewController("/403").setViewName("403");
    }
}
