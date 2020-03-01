package com.dez.predesign.repository;

import com.dez.predesign.data.catalog.Brand;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BrandRepo extends CrudRepository<Brand,String> {
    Iterable<Brand> findByName(String name);
}
