package com.dez.predesign.data.catalog;

import javax.persistence.*;

@Entity
@Table(name="Brands")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
}
