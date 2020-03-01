package com.dez.predesign.controller.admin.Product;

import com.dez.predesign.data.catalog.Color;
import com.dez.predesign.repository.ColorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admins/product/colors")
public class ProductColorController {

    @Autowired
    ColorRepo colorRepo;

    @ModelAttribute(name = "colors")
    public Iterable<Color> colors() {
        return colorRepo.findAll();
    }

    @GetMapping
    public String showColors(Model model){

        return "/admins/productColors";
    }

    @PostMapping("delete")
    public String deleteCOlor(@RequestParam Color color){

        colorRepo.delete(color);

        return "redirect:/admins/product/colors";
    }

    @PostMapping("add")
    public String addCOlor(@RequestParam String r,
                           @RequestParam String g,
                           @RequestParam String b,
                           Model model){

        Integer r_i = Integer.parseInt(r);
        Integer g_i = Integer.parseInt(g);
        Integer b_i =Integer.parseInt(b);

        if(r_i < 0 || r_i > 255)
            model.addAttribute("rError", "r  must be between 0 - 255");
        if(g_i < 0 || g_i > 255)
            model.addAttribute("gError", "g  must be between 0 - 255");
        if(b_i < 0 || b_i > 255)
            model.addAttribute("bError", "b  must be between 0 - 255");

        if(model.containsAttribute("rError")
                || model.containsAttribute("gError")
                || model.containsAttribute("bError")
        ) return "/admins/productColors";

        Color color = new Color(r + ", " + g + ", " + b);

        if(colorRepo.findByRgb(color.getRgb()) != null) {
            model.addAttribute("colorError", "color already exist");
            return "/admins/productColors";
        }

        colorRepo.save(color);

        return "redirect:/admins/product/colors";
    }
}
