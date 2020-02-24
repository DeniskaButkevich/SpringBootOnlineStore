package com.dez.predesign.repository;

import com.dez.predesign.data.catalog.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface  CategoryRepo extends CrudRepository<Category,Long> {

    List<Category> findByLevel(Integer level);

    List<Category> findByLevelAndDescendant(Integer level, Category category);

    Category findByNameAndLevelAndDescendant(String name,Integer level, Category category);
}
