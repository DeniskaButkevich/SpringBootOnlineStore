package com.dez.predesign.data.catalog;

import javax.persistence.*;

@Entity
@Table(name="Colors")
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
}
