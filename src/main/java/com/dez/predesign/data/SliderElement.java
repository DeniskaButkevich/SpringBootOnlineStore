package com.dez.predesign.data;

import com.dez.predesign.data.catalog.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@NoArgsConstructor
@Data
public class SliderElement {

    @Id
    private Integer id;

    @OneToOne(targetEntity = Product.class)
    private Product product;

    private String filename;

    private Integer number;

    public SliderElement(Integer id, Product product, Integer number){
        this.id = id;
        this.product = product;
        this.number = number;
    }
}
