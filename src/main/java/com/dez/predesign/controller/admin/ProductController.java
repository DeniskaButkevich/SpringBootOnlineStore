package com.dez.predesign.controller.admin;

import com.dez.predesign.data.Order;
import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.repository.ImageRepo;
import com.dez.predesign.repository.ProductRepo;
import com.dez.predesign.service.PageService;
import com.dez.predesign.service.ProductService;
import com.dez.predesign.util.ControllerUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class ProductController {
    @Value("${AWS_SECRET_ACCESS_KEY}")
    public String AWS_SECRET_ACCESS_KEY;
    @Value("${AWS_ACCESS_KEY_ID}")
    private String AWS_ACCESS_KEY_ID;
    @Value("${S3_BUCKET_NAME}")
    private String S3_BUCKET_NAME;

    private ProductRepo productRepo;
    private ProductService productService;
    private PageService pageService;

    public ProductController(
            ProductRepo productRepo,
            ProductService productService,
            PageService pageService) {
        this.productRepo = productRepo;
        this.productService = productService;
        this.pageService = pageService;
    }

    @GetMapping("/admins/product")
    public String productShow(@RequestParam(required = false, defaultValue = "") String filter,
                              @RequestParam(required = false, defaultValue = "") String search_by,
                              @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                              Model model) {
        Page<Product> page;
        if(filter != null && !filter.isEmpty() && search_by != null){
            page = pageService.findByFilterProducts(search_by, filter, pageable);
        } else {
            page = productRepo.findAll(pageable);
        }

        List<Integer> listpages = pageService.listPages(page);

        model.addAttribute("listpages", listpages);
        model.addAttribute("page", page);
        model.addAttribute("url", "/admins/product");
        model.addAttribute("filter", filter);

        return "admins/productList";
    }

    @PostMapping("/admins/product")
    public String productAdd(@Valid Product product, BindingResult bindingResult, Model model,
                             @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 3) Pageable pageable) {
        String return_url;

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            errorsMap.put("hasErrors", "true");
            model.mergeAttributes(errorsMap);
            model.addAttribute("product", product);
            return_url = "admins/productList";
        } else {
            model.addAttribute("product", null);
            product.setNewProduct(true);
            productRepo.save(product);
            return_url = "redirect:/admins/product";
        }
        Page<Product> page = productRepo.findAll(pageable);
        List<Integer> listpages = pageService.listPages(page);

        model.addAttribute("listpages", listpages);
        model.addAttribute("page", page);

        return return_url;
    }

    @GetMapping("/product/delete")
    public String productRemove(@RequestParam String id) {
        productService.deleteOne(id);
        return "redirect:/admins/product";
    }

    @GetMapping("/admins/products_order")
    public String productForOrder(@RequestParam(required = false, defaultValue = "") Order filter,
                                  HttpServletRequest request,
                                  Model model) {

        Set<Product> products = filter.getProducts();
        model.addAttribute("products", products);
        String url = pageService.getAbsolutePath(request);

        model.addAttribute("url", url);

        return "admins/productForOrder";
    }
}
