package com.dez.predesign.data;

import com.dez.predesign.data.catalog.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
public class Slide {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToOne(targetEntity = Product.class)
    private Product product;
    private String filename;

    public Slide(Product product, String filename) {
        this.product = product;
        this.filename = filename;
    }
}
