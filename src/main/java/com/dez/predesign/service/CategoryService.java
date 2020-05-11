package com.dez.predesign.service;

import com.dez.predesign.data.catalog.Brand;
import com.dez.predesign.data.catalog.Category;
import com.dez.predesign.data.catalog.Color;
import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.repository.BrandRepo;
import com.dez.predesign.repository.CategoryRepo;
import com.dez.predesign.repository.ColorRepo;
import com.dez.predesign.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Service
public class CategoryService {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    ColorRepo colorRepo;

    @Autowired
    BrandRepo brandRepo;


    public String setUrlParams(Map<String, String> allRequestParams) {
        String setParam = "";
        if (allRequestParams.size() != 0) {
            Set<String> keySet = allRequestParams.keySet();
            for (String key : keySet) {
                String param = allRequestParams.get(key);
                setParam = setParam + key + "=" + param + "&";
            }
        }
        return setParam;
    }

    public Pageable sortByFilter(Pageable pageable, Map<String, String> allRequestParams) {
        if (allRequestParams.get("filter") != null && !allRequestParams.get("filter").isEmpty()){
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(allRequestParams.get("filter"))
            );
        }
        return pageable;
    }

    public Product getProduct() {
        List<Long> lds = productRepo.findAllId();
        if(lds.size() > 0){
            Random rand = new Random();
            Long randomElement = lds.get(rand.nextInt(lds.size()));

            return productRepo.findOneProduct(randomElement);
        }
        return  null;
    }
}
