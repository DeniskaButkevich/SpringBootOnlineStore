package com.dez.predesign.repository;

import com.dez.predesign.data.catalog.Size;
import org.springframework.data.repository.CrudRepository;

public interface SizeRepo extends CrudRepository<Size,String> {
    Size findBySize(String size);
}
