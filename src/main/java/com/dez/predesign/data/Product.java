package com.dez.predesign.data;

import com.dez.predesign.data.catalog.Brand;
import com.dez.predesign.data.catalog.Category;
import com.dez.predesign.data.catalog.Color;
import com.dez.predesign.data.catalog.Image;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private Double price;

    private String filename;
    private String hoverFilename;

    @ManyToOne(targetEntity= Brand.class)
    private Brand brand;
    @ManyToOne(targetEntity= Color.class)
    private Color color;

    @ManyToOne(targetEntity= Category.class)
    private String category;
    private String type;

    private Integer sale;
    private boolean newProduct;
}
