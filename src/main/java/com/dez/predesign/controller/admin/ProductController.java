package com.dez.predesign.controller.admin;

import com.dez.predesign.data.Order;
import com.dez.predesign.data.User;
import com.dez.predesign.data.catalog.*;
import com.dez.predesign.repository.*;
import com.dez.predesign.service.PageService;
import com.dez.predesign.service.ProductService;
import com.dez.predesign.util.ControllerUtils;
import com.dez.predesign.util.UploadImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class ProductController {

    private ProductRepo productRepo;
    private BrandRepo brandRepo;
    private ColorRepo colorRepo;
    private ProductService productService;
    private PageService pageService;
    private CategoryRepo categoryRepo;
    private ImageRepo imageRepo;
    private OrderRepo orderRepo;
    private SizeRepo sizeRepo;

    public ProductController(
            ProductRepo productRepo,
            BrandRepo brandRepo,
            ColorRepo colorRepo,
            ProductService productService,
            PageService pageService,
            CategoryRepo categoryRepo,
            ImageRepo imageRepo,
            OrderRepo orderRepo,
            SizeRepo sizeRepo){
        this.sizeRepo = sizeRepo;
        this.imageRepo = imageRepo;
        this.brandRepo = brandRepo;
        this.categoryRepo = categoryRepo;
        this.orderRepo = orderRepo;
        this.colorRepo = colorRepo;
        this.productRepo = productRepo;
        this.productService = productService;
        this.pageService = pageService;
    }

    @Value("${AWS_ACCESS_KEY_ID}")
    private String AWS_ACCESS_KEY_ID;

    @Value("${AWS_SECRET_ACCESS_KEY}")
    public String AWS_SECRET_ACCESS_KEY;

    @Value("${S3_BUCKET_NAME}")
    private String S3_BUCKET_NAME;

    @ModelAttribute(name = "brands")
    public Iterable<Brand> brands() {
        return brandRepo.findAll();
    }

    @ModelAttribute(name = "colors")
    public Iterable<Color> colors() {
        return colorRepo.findAll();
    }

    @ModelAttribute(name = "sizes")
    public Iterable<Size> sizes() {
        return sizeRepo.findAll();
    }

    @ModelAttribute(name = "categoriesLevelOne")
    public List<Category> setCategoriesLevelOne() {
        return categoryRepo.findByLevelAndDescendant(1, null);
    }

    @ModelAttribute(name = "categoriesLevelTwo")
    public List<Category> setCategoriesLevelTwo() {
        return categoryRepo.findByLevel(2);
    }

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/admins/product")
    public String productShow(@RequestParam(required = false, defaultValue = "") Brand filter,
                              @RequestParam(required = false, defaultValue = "") String id,
                              @PageableDefault(sort = {"id"},direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                              Model model) {
        Page<Product> page;

        if (filter != null && !filter.getName().isEmpty()){
            page = productRepo.findByBrand(pageable, filter);
        }else if(id != null && !id.isEmpty()){
            page = productRepo.findById(pageable, Long.parseLong(id));
        }else{
            page = productRepo.findAll(pageable);
        }
        List<Integer> listpages = pageService.listPages(page);

        model.addAttribute("listpages", listpages);
        model.addAttribute("page", page);
        model.addAttribute("url", "/admins/product");
        model.addAttribute("filter", filter);

        return "admins/productList";
    }

    @GetMapping("/admins/products_order")
    public String productForOrder(@RequestParam(required = false, defaultValue = "") Order filter,
                                  HttpServletRequest request,
                                  Model model){

        Set<Product> products = filter.getProducts();
        model.addAttribute("products", products);
        String url = pageService.getAbsolutePath(request);

        model.addAttribute("url", url);

        return "admins/productForOrder";
    }
    @PostMapping("/admins/product")
    public String productAdd(@AuthenticationPrincipal User user,
                             @Valid Product product,
                             BindingResult bindingResult,
                             Model model,
                             @PageableDefault(sort = {"id"},direction = Sort.Direction.DESC, size = 3) Pageable pageable) throws IOException {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);
            model.addAttribute("product", product);
        } else {
            model.addAttribute("product", null);
            product.setNewProduct(true);
            productRepo.save(product);
        }
        Page<Product> page = productRepo.findAll(pageable);
        List<Integer> listpages = pageService.listPages(page);

        model.addAttribute("listpages", listpages);
        model.addAttribute("page", page);

        return "redirect:/admins/product";
    }

    @GetMapping("/product/delete")
    public String productRemove(@RequestParam String id) {
        Product product = productRepo.findById(Long.parseLong(id)).get();

        Iterable<Image> images= imageRepo.findByProduct(product);
        for (Image image: images){
            imageRepo.delete(image);
            UploadImage.deleteObjectAmazonS3(image.getName(), S3_BUCKET_NAME, AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
        }
        productRepo.delete(
                productRepo.findById(
                        Long.parseLong(id)).get()
        );
        return "redirect:/admins/product";
    }

    @PostMapping("/product/edit/{id}")
    public String productEdit(@PathVariable String id, Product productGet){
        Product productSet = productRepo.findById( Long.parseLong(id)).get();
        productService.setProduct(productGet, productSet);
        productRepo.save(productSet);

        return "redirect:/admins/product";
    }
}
