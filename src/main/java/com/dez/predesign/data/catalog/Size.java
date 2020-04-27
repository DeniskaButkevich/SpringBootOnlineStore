package com.dez.predesign.data.catalog;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sizes")
@Data
public class Size {
    @Id
    private String size;

    public Size(){}

    public Size(String size) {
        this.size = size;
    }
}
