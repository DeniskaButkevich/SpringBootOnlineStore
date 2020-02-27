package com.dez.predesign.data.catalog;

import com.dez.predesign.data.Product;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="Images")
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "product_id")
    private Product product;

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    private String name;

    public Image(Product product, String name) {
        this.product = product;
        this.name = name;
    }

    public Image (){}

}
