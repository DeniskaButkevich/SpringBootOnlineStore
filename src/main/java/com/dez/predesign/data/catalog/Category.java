package com.dez.predesign.data.catalog;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name="Categorys")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;

    @OneToMany
    //@Size(min=1, message="You must choose at least 1 ingredient")
    private List<Type> types;
}
