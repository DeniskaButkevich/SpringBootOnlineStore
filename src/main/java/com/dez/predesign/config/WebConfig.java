package com.dez.predesign.config;

import com.dez.predesign.data.Role;
import com.dez.predesign.data.User;
import com.dez.predesign.repository.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.constraints.Null;
import java.util.Collections;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/aboutus.html").setViewName("aboutus");
        registry.addViewController("/blog.html").setViewName("blog");
        registry.addViewController("/cart.html").setViewName("cart");
        registry.addViewController("/category.html").setViewName("category");
        registry.addViewController("/category-list.html").setViewName("category-list");
        registry.addViewController("/compare-products.html").setViewName("compare-products");
        registry.addViewController("/contact.html").setViewName("contact");
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/portfolio-2.html").setViewName("portfolio-2");
        registry.addViewController("/portfolio-masonry-2.html").setViewName("portfolio-masonry-2");
        registry.addViewController("/product.html").setViewName("product");
        registry.addViewController("/register-account.html").setViewName("register-account");
        registry.addViewController("/single.html").setViewName("single");
        registry.addViewController("/single-portfolio.html").setViewName("single-portfolio");
        registry.addViewController("/single-portfolio-gallery.html").setViewName("single-portfolio-gallery");
        registry.addViewController("/single-portfolio-video.html").setViewName("single-portfolio-video");
        registry.addViewController("/checkout.html").setViewName("checkout");
        registry.addViewController("/admin/userList").setViewName("/admins/userList");
        registry.addViewController("/admin/indexAdmin").setViewName("/admins/indexAdmin");
        registry.addViewController("/404.html").setViewName("404");
    }

//    @Bean
//    public CommandLineRunner dataLoader(UserRepo userRepo) { // user repo for ease of testing with a built-in user
//
//
//        return new CommandLineRunner() {
//
//            @Override
//            public void run(String... args) throws Exception {
//                User user = new User();
//                user.setActive(true);
//                user.setUsername("3");
//                user.setFirstName("3");
//                user.setPassword("3");
//                user.setRoles(Collections.singleton(Role.USER));
//                userRepo.save(user);
//            }
//        };
//    }
}
