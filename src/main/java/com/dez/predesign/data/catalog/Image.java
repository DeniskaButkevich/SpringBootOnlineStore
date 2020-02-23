package com.dez.predesign.data.catalog;

import com.dez.predesign.data.Product;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="Images")
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = Product.class)
    private Product product;

    private String name;

    public Image (){}
    public Image( String name, Product product) {
        this.product = product;
        this.name = name;
    }
}
