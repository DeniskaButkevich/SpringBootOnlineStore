package com.dez.predesign.repository;

import com.dez.predesign.data.catalog.Color;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface ColorRepo extends CrudRepository<Color,String> {
    Color findByRgb(String rgb);
}
