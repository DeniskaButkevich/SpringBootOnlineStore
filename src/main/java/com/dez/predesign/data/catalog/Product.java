package com.dez.predesign.data;

import com.dez.predesign.data.catalog.Brand;
import com.dez.predesign.data.catalog.Category;
import com.dez.predesign.data.catalog.Color;
import com.dez.predesign.data.catalog.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Please fill the name")
    @Size(max=30, message = "length should be no more than 30")
    private String name;

    @NotNull(message = "Please fill the price ")
    @Min(value = 0, message = "price should be more than 0")
    private Double price;

    @Column(length = 2048)
    private String description;
    private String filename;
    private String hoverFilename;

    @OneToMany(targetEntity = Image.class, mappedBy = "product")
    private List<Image> imageList;

    @ManyToOne(targetEntity= Brand.class)
    private Brand brand;
    @ManyToOne(targetEntity= Color.class)
    private Color color;

    @JoinColumn(name = "category_id")
    @ManyToOne(targetEntity= Category.class, fetch = FetchType.LAZY)
    private Category category;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Min(value = 0, message = "sale should be more than 0")
    @Max(value = 100, message = "sale should be less than 100")
    private Integer sale;
    private boolean newProduct;
}