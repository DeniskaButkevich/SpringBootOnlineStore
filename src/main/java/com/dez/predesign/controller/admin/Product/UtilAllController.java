package com.dez.predesign.controller.admin.Product;

import com.dez.predesign.controller.admin.ProductController;
import com.dez.predesign.data.catalog.Brand;
import com.dez.predesign.data.catalog.Category;
import com.dez.predesign.data.catalog.Color;
import com.dez.predesign.data.catalog.Size;
import com.dez.predesign.repository.BrandRepo;
import com.dez.predesign.repository.CategoryRepo;
import com.dez.predesign.repository.ColorRepo;
import com.dez.predesign.repository.SizeRepo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice(assignableTypes = {AdminsProductController.class, ProductController.class})
public class UtilAllController {

    private BrandRepo brandRepo;
    private ColorRepo colorRepo;
    private CategoryRepo categoryRepo;
    private SizeRepo sizeRepo;

    public UtilAllController(
            BrandRepo brandRepo,
            ColorRepo colorRepo,
            CategoryRepo categoryRepo,
            SizeRepo sizeRepo) {
        this.sizeRepo = sizeRepo;
        this.brandRepo = brandRepo;
        this.categoryRepo = categoryRepo;
        this.colorRepo = colorRepo;
    }

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

}
