package com.dez.predesign.data.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Params {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Product product;

    @ManyToOne
    private Size size;

    @ManyToOne
    private Color color;


    public Params(Product product, Size size, Color color) {
        this.product = product;
        this.size = size;
        this.color = color;
    }
}
