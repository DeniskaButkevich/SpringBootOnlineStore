package com.dez.predesign.repository;

import com.dez.predesign.data.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepo extends CrudRepository<Product,Integer> {
    Page<Product> findByBrand(String tag, Pageable pageable);

    Page<Product> findAll(Pageable pageable);
}
