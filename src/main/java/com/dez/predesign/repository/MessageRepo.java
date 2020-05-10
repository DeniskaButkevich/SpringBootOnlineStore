package com.dez.predesign.repository;

import com.dez.predesign.data.Message;
import com.dez.predesign.data.User;
import com.dez.predesign.data.catalog.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepo extends CrudRepository<Message,Long>{

    Page<Message> findAll(Pageable pageable);

    Page<Message> findByAuthor(Pageable pageable, User author);

    Page<Message> findByProduct(Pageable pageable, Product product);
}
