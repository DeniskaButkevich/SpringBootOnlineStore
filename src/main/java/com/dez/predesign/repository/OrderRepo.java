package com.dez.predesign.repository;

import com.dez.predesign.data.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepo extends CrudRepository<Order, Long> {
    Page<Order> findAll(Pageable pageable);
}
