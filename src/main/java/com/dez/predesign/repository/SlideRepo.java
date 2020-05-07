package com.dez.predesign.repository;

import com.dez.predesign.data.Slide;
import com.dez.predesign.data.catalog.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface SlideRepo extends CrudRepository<Slide, Product> {
    Page<Slide> findAll(Pageable pageable);
    Slide findById(Integer id);
}
