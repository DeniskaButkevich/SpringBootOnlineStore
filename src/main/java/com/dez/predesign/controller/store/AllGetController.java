package com.dez.predesign.controller.store;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/orderSuccessful")
    public String orderSuccessful(){
        return "orderSuccessful";
    }
}
