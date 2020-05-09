package com.dez.predesign.data;

import com.dez.predesign.data.catalog.Params;
import com.dez.predesign.data.catalog.Product;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity= User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(targetEntity = Product.class)
    private Set<Product> products;

    private Date placedAt;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "order_product_mapping",
            joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "countAndPrice_id", referencedColumnName = "id")})
    @MapKeyJoinColumn(name = "product_id")
    private Map<Product,CountAndPrice> count_price;

    private Integer active;

    private Double total_price;

    @OneToMany
    private List<Params> params;

    @PrePersist
    void placedAt() {
        this.placedAt = new Date();
    }



}