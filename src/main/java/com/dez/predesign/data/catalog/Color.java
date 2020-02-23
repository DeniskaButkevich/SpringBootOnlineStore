package com.dez.predesign.data.catalog;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="Colors")
@Data
public class Color {
    @Id
    private String rgb;
}
