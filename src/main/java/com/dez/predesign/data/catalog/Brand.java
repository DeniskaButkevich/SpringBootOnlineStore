package com.dez.predesign.data.catalog;

import lombok.Data;

import javax.persistence.*;

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
}
