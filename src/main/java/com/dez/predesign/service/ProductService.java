package com.dez.predesign.service;

import com.dez.predesign.data.Message;
import com.dez.predesign.data.User;
import com.dez.predesign.data.catalog.Image;
import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.repository.ImageRepo;
import com.dez.predesign.repository.ProductRepo;
import com.dez.predesign.repository.UserRepo;
import com.dez.predesign.util.UploadImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

@Service
public class ProductService {
    @Value("${AWS_ACCESS_KEY_ID}")
    private String AWS_ACCESS_KEY_ID;
    @Value("${S3_BUCKET_NAME}")
    private String S3_BUCKET_NAME;
    @Value("${AWS_SECRET_ACCESS_KEY}")
    public String AWS_SECRET_ACCESS_KEY;

    private ProductRepo productRepo;
    private ImageRepo imageRepo;
    private org.springframework.jdbc.core.JdbcTemplate JdbcTemplate;

    public ProductService(ProductRepo productRepo, ImageRepo imageRepo, org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
        this.productRepo = productRepo;
        this.imageRepo = imageRepo;
        this.JdbcTemplate = jdbcTemplate;
    }

    public void setProduct(Product productGet, Product productSet) {
        productSet.setName(productGet.getName());
        productSet.setBrand(productGet.getBrand());
        productSet.setColor(productGet.getColor());
        productSet.setDescription(productGet.getDescription());
        productSet.setNewProduct(productGet.isNewProduct());
        productSet.setPrice(productGet.getPrice());
        productSet.setSale(productGet.getSale());
    }

    @Transactional
    public Iterable<Product> getViewedProducts(Integer user_id, UserRepo userRepo) {
        return userRepo.findById(user_id).get().getProducts();
    }

    @Transactional
    public boolean addViewedProduct(Integer user_id, Product product, UserRepo userRepo) {
            User user = userRepo.findById(user_id).get();
            Set<Product> set = user.getProducts();
            if (!set.contains(product)) {
                set.add(product);
                if (set.size() > 3) {
                    Iterator iterator = set.iterator();
                    if(iterator.hasNext()){
                        iterator.next();
                        iterator.remove();
                    }
                }
                userRepo.save(user);
                return true;
            }
            return false;
    }

    public void update(Product old_product, Product product) {
        old_product.setNewProduct(true);
        old_product.setBrand(product.getBrand());
        old_product.setColor(product.getColor());
        old_product.setDescription(product.getDescription());
        old_product.setName(product.getName());
        old_product.setPrice(product.getPrice());
        old_product.setSale(product.getSale());
        old_product.setCategory(product.getCategory());
        old_product.setSizes(product.getSizes());
    }

    public void deleteOne(String id) {
        Product product = productRepo.findById(Long.parseLong(id)).get();

        Iterable<Image> images = imageRepo.findByProduct(product);
        for (Image image : images) {
            imageRepo.delete(image);
            UploadImage.deleteObjectAmazonS3(image.getName(), S3_BUCKET_NAME, AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
        }
        JdbcTemplate.update("Delete from user_products where product_id = ?",Long.parseLong(id));


        productRepo.delete(
                productRepo.findById(
                        Long.parseLong(id)).get()
        );
    }

    public Integer getRating(Product product) {
        if(product.getMessages() != null && product.getMessages().size() > 0){
            Double rating = 0D;
            for (Message message: product.getMessages()){
                rating += message.getRating();
            }
            rating = rating/product.getMessages().size()*20;

            Integer result =  rating.intValue();
            return result;
        }
        return null;
    }
}
