package com.dez.predesign.repository;

import com.dez.predesign.data.catalog.Image;
import com.dez.predesign.data.catalog.Product;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepo extends CrudRepository<Image,Long> {
    Iterable<Image>
    findByProduct(Product product);
    Image findByName(String name);
}
