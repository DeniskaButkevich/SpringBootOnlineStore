package com.dez.predesign.data.catalog;

import javax.persistence.*;

@Entity
@Table(name="Types")
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
}
