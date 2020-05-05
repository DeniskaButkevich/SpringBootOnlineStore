package com.dez.predesign.data.catalog;

import com.dez.predesign.data.Order;
import com.dez.predesign.data.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @NotBlank(message = "Please fill the name")
    @javax.validation.constraints.Size(max=30, message = "length should be no more than 30")
    private String name;

    @NotNull(message = "Please fill the price ")
    @Min(value = 0, message = "price should be more than 0")
    private Double price;

    @Column(length = 2048)
    @javax.validation.constraints.Size(max=2048, message = "length should be no more than 30")
    private String description;
    private String filename;
    private String hoverFilename;

    @OneToMany(targetEntity = Image.class, mappedBy = "product")
    private List<Image> imageList;

    @ManyToOne(targetEntity= Brand.class)
    private Brand brand;
    @ManyToMany(targetEntity= Color.class)
    private Set<Color> color;

    @ManyToMany(targetEntity= Size.class)
    private Set<Size> sizes;

    @JoinColumn(name = "category_id")
    @ManyToOne(targetEntity= Category.class, fetch = FetchType.LAZY)
    private Category category;

    @Min(value = 0, message = "sale should be more than 0")
    @Max(value = 100, message = "sale should be less than 100")
    private Integer sale;
    private boolean newProduct;

    @ManyToMany
    private Set<Order> orders;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}