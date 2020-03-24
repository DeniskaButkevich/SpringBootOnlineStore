package com.dez.predesign.repository;

import com.dez.predesign.data.SliderElement;
import com.dez.predesign.data.catalog.Product;
import org.springframework.data.repository.CrudRepository;

public interface SliderElementRepo extends CrudRepository<SliderElement, Product> {

    SliderElement findByNumber(Integer id);
}
