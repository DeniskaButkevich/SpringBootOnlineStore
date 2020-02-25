package com.dez.predesign.data.catalog;

import com.dez.predesign.data.Product;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
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
