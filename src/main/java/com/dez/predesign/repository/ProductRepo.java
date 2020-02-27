package com.dez.predesign.repository;

import com.dez.predesign.data.Product;
import com.dez.predesign.data.catalog.Brand;
import com.dez.predesign.data.catalog.Category;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepo extends CrudRepository<Product,Long>, JpaRepository<Product,Long> {
    Page<Product> findByBrand(Brand brand, Pageable pageable);
    Page<Product> findByCategory(Category category, Pageable pageable);
    Page<Product> findByCategory_Ancestor(Category categories ,Pageable pageable);
    Page<Product> findAll(Example example, Pageable pageable);
    Page<Product> findAll(Pageable pageable);
    List<Product> findAll(Example example);

}
