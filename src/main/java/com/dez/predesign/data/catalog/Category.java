package com.dez.predesign.data.catalog;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.util.List;

@Entity
@Data
@Table( name="categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToOne(targetEntity = Category.class)
    private Category ancestor;

    @ManyToOne(targetEntity = Category.class, optional = false)
    private Category descendant;

    @OneToMany(targetEntity = Product.class, mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products;

    private Integer level;

    public Category(String name, Category descendant, Integer level) {
        this.name = name;
        this.descendant = descendant;
        this.level = level;
    }

    public Category(String name, Integer level, Category ancestor) {
        this.name = name;
        this.ancestor = ancestor;
        this.level = level;
    }

    public Category(String name, Integer level) {
        this.name = name;
        this.level = level;
    }

    public Category() {
    }
}
