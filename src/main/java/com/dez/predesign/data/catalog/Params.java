package com.dez.predesign.data.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Params {
    @Id
    private Long Id;

    @ManyToOne
    private Size size;

    @ManyToOne
    private Color color;
}
