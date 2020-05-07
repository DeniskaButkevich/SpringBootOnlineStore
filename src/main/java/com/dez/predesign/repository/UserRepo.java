package com.dez.predesign.repository;

import com.dez.predesign.data.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;


public interface UserRepo extends CrudRepository<User, Integer> {
    User findByUsername(String username);

    User findByActivationCode(String code);

    Page<User> findAll(Pageable pageable);
}
