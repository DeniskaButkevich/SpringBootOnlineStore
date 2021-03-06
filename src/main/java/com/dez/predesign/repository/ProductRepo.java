package com.dez.predesign.repository;

import com.dez.predesign.data.catalog.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ProductRepo extends JpaRepository<Product,Long> {
    Page<Product> findByBrand(Pageable pageable,Brand brand);

    Page<Product> findByName(Pageable pageable, String name);

    Page<Product> findById(Pageable pageable,Long brand);

    Page<Product> findById(Pageable pageable,String brand);

    Page<Product> findByPrice(Pageable pageable, Double price);

    Page<Product> findBySaleNotNull(Pageable pageable);

    Page<Product> findBySale(Pageable pageable, Integer sale);

    Page<Product> findByCategory(Pageable pageable, Category category);

    Page<Product> findBySizes(Pageable pageable, Size size);

    @Query(value = "SELECT p FROM Product  p WHERE p.category IN :categories and p.brand IN :brand")
    Page<Product> findBC(Pageable pageable, @Param("categories") Iterable<Category> categories, @Param("brand") Iterable<Brand> brand);

    @Query(value = "SELECT p FROM Product  p WHERE p.brand In :brand")
    Page<Product> findB(Pageable pageable, @Param("brand") Iterable<Brand> brand);

    @Query(value = "SELECT p FROM Product  p WHERE p.category IN :categories")
    Page<Product> findC(Pageable pageable, @Param("categories") Iterable<Category> names);

    @Query(value = "select p FROM Product p where p.id = :ids")
    Product findOneProduct(@Param("ids") Long ids);

    @Query(value ="SELECT p.id from Product p")
    List<Long> findAllId();

    @Query(value = "SELECT p FROM Product p WHERE p.id IN :ids")
    Iterable<Product> findAllProductsByIds( List<Long> ids);

//    List<Product> getData(Map<String, String> conditions);
}


