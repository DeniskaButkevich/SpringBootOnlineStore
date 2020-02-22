package com.dez.predesign.data;

import com.dez.predesign.data.catalog.Brand;
import com.dez.predesign.data.catalog.Category;
import com.dez.predesign.data.catalog.Color;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    private String description;
    private Double price;

    private String filename;

    @ManyToOne(targetEntity= Brand.class)
    private String brand;
    @ManyToOne(targetEntity= Color.class)
    private String color;
    @ManyToOne(targetEntity= Category.class)
    private String category;

    private String type;

    private Integer sale;
    private boolean newProduct;
}
