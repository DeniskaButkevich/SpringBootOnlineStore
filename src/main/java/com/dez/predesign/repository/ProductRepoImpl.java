package com.dez.predesign.repository;


import com.dez.predesign.data.catalog.Brand;
import com.dez.predesign.data.catalog.Category;
import com.dez.predesign.data.catalog.Color;
import com.dez.predesign.data.catalog.Product;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProductRepoImpl {

    private BrandRepo brandRepo;
    private CategoryRepo categoryRepo;
    private ColorRepo colorRepo;

    public ProductRepoImpl(BrandRepo brandRepo, CategoryRepo categoryRepo, ColorRepo colorRepo) {
        this.brandRepo = brandRepo;
        this.categoryRepo = categoryRepo;
        this.colorRepo = colorRepo;
    }

    public List<Product> getData(Map<String, String> conditions, EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        List<Predicate> predicates = new ArrayList<>();

        conditions.forEach((field, value) ->
        {
            switch (field) {
                case "brand":
                    predicates.add(cb.equal(root.<Brand>get(field), brandRepo.findByName(value)));
                    break;
                case "color":
                    predicates.add(cb.equal(root.join("color"), colorRepo.findByRgb(value)));
                    break;
                case "category":
                    predicates.add(cb.equal(root.<Category>get(field), categoryRepo.findById(Long.parseLong(value)).get()));
                    break;
                case "price-range-low":
                    predicates.add(cb.greaterThan(root.get("price"), Double.parseDouble(value)));
                    break;
                case "price-range-high":
                    predicates.add(cb.lessThan(root.get("price"), Double.parseDouble(value)));
                    break;
            }
        });
        query.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
        return entityManager.createQuery(query).getResultList();
    }
}