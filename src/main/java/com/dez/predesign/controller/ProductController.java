package com.dez.predesign.controller;

import com.dez.predesign.data.Product;
import com.dez.predesign.data.User;
import com.dez.predesign.repository.ProductRepo;
import com.dez.predesign.service.PageService;
import com.dez.predesign.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class ProductController {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    ProductService productService;

    @Autowired
    PageService pageService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/admins/product")
    public String productShow( @RequestParam(required = false, defaultValue = "") String filter,
                                Model model,
                                @PageableDefault(sort = {"id"},direction = Sort.Direction.DESC, size = 3) Pageable pageable) {
        Page<Product> page;

        if (filter != null && !filter.isEmpty())
            page = productRepo.findByBrand(filter, pageable);
        else
            page = productRepo.findAll(pageable);

        List<Integer> listpages = pageService.listPages(pageable, page);

        model.addAttribute("listpages", listpages);
        model.addAttribute("page", page);
        model.addAttribute("url", "/admins/product");
        model.addAttribute("filter", filter);

        return "admins/productList";
    }

    @PostMapping("/admins/product")
    public String productAdd(@AuthenticationPrincipal User user,
                             @Valid Product product,
                             BindingResult bindingResult,
                             Model model,
                             @RequestParam("file") MultipartFile file,
                             @PageableDefault(sort = {"id"},direction = Sort.Direction.DESC, size = 3) Pageable pageable
    ) throws IOException {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);
            model.addAttribute("product", product);

        } else {
            if (file != null && !file.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(uploadPath + "products\\");

                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + file.getOriginalFilename();


                file.transferTo(new File("C:\\Desktop\\java\\sweater\\uploads\\products\\" + resultFilename));

                product.setFilename(resultFilename);
            }

            model.addAttribute("product", null);
            productRepo.save(product);
        }
        Page<Product> page = productRepo.findAll(pageable);
        List<Integer> listpages = pageService.listPages(pageable, page);

        model.addAttribute("listpages", listpages);

        model.addAttribute("page", page);

        return "admins/productList";
    }

    @GetMapping("/product/delete")
    public String productRemove(@RequestParam String id) {

        productRepo.delete(
                productRepo.findById(
                        Integer.parseInt(id)).get()
        );

        return "redirect:/admins/product";
    }

    @PostMapping("/product/edit/{id}")
    public String productEdit(@PathVariable String id, Product product){

        Product item = productRepo.findById(Integer.parseInt(id)).get();

        item.setName(product.getName());
        item.setBrand(product.getBrand());
        item.setColor(product.getColor());
        item.setDescription(product.getDescription());
        item.setNewProduct(false);
        item.setPrice(product.getPrice());
        item.setSale(product.getSale());

        productRepo.save(item);

        return "redirect:/admins/product";
    }
}
