package com.dez.predesign.repository;

import com.dez.predesign.data.SliderElement;
import com.dez.predesign.data.catalog.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface SliderElementRepo extends CrudRepository<SliderElement, Product> {

    Page<SliderElement> findAll(Pageable pageable);

    SliderElement findByNumber(Integer id);
}
