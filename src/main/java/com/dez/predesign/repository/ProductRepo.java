package com.dez.predesign.repository;

import com.dez.predesign.data.Product;
import com.dez.predesign.data.catalog.Brand;
import com.dez.predesign.data.catalog.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepo extends CrudRepository<Product,Long> {
    Page<Product> findByBrand(Brand brand, Pageable pageable);
    Page<Product> findByCategory(Category category, Pageable pageable);
    Page<Product> findByCategory_Ancestor(Category categories ,Pageable pageable);
    Page<Product> findAll(Pageable pageable);
}
