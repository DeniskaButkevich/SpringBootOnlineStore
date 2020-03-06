package com.dez.predesign.data.catalog;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="Brands")
@Data
public class Brand {
    @Id
    private String name;

    public Brand(String name) {
        this.name = name;
    }

    public Brand() {
    }

    @OneToMany(targetEntity = Product.class, mappedBy = "brand", fetch = FetchType.LAZY)
    private List<Product> products;
}
