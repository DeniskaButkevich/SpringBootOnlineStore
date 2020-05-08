package com.dez.predesign.repository;

import com.dez.predesign.data.catalog.Params;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParamsRepo  extends CrudRepository<Params,Long> {
}
