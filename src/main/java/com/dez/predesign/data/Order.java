package com.dez.predesign.data;

import com.dez.predesign.data.catalog.Product;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity= User.class)
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

    private Double total_price;

    @PrePersist
    void placedAt() {
        this.placedAt = new Date();
    }

}
