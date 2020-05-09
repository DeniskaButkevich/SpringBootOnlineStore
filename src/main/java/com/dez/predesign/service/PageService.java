package com.dez.predesign.service;

import com.dez.predesign.data.Order;
import com.dez.predesign.data.User;
import com.dez.predesign.data.catalog.Brand;
import com.dez.predesign.data.catalog.Category;
import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.data.catalog.Size;
import com.dez.predesign.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PageService {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    BrandRepo brandRepo;

    @Autowired
    SizeRepo sizeRepo;

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    UserRepo userRepo;

    public List<Integer> listPages(Page page) {
        if(page == null){
            return null;
        }
        if(page.getTotalPages() > 7){

            Integer totalPages = page.getTotalPages();
            Integer pageNumber = page.getNumber() + 1;

            List<Integer> head = (pageNumber > 4) ? Arrays.asList(1,-1) :Arrays.asList(1, 2, 3);
            List<Integer> tail = (pageNumber < totalPages - 3) ? Arrays.asList(-1, totalPages) : Arrays.asList(totalPages - 2, totalPages -1, totalPages);
            List<Integer> bodyBefore = (pageNumber > 4 && pageNumber < totalPages - 1) ? Arrays.asList(pageNumber - 2, pageNumber - 1) : Arrays.asList();
            List<Integer> bodyAfter = (pageNumber > 2 && pageNumber < totalPages - 3) ?  Arrays.asList(pageNumber + 1, pageNumber + 2) : Arrays.asList();

            List<Integer> result = new ArrayList<>();;

            result.addAll(head);
            result.addAll(bodyBefore);
            result.addAll((pageNumber > 3 && pageNumber < totalPages - 2) ?  Arrays.asList(pageNumber)  : Arrays.asList()) ;
            result.addAll(bodyAfter);
            result.addAll(tail);

            return result;
        }
        List<Integer> result = new ArrayList<>();
        for (int i = 1; i <= page.getTotalPages(); i++)
            result.add(i);

        return result;
    }

    public String getAbsolutePath(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        String path = request.getContextPath();
        String basePath = scheme + "://" + serverName + ":" + port + path;
        return basePath;
    }

    public Page<Product> findByFilterProducts(String search_by, String filter, Pageable pageable) {

        Page<Product> page = null;
            if(search_by.equals("Id")) {
                Long id = Long.parseLong(filter);
                page = productRepo.findById(pageable, id);
            }else if(search_by.equals("Name")){
                page = productRepo.findByName(pageable, filter);
            }else if(search_by.equals("Brand")){
                Iterable<Brand> brand = brandRepo.findByName(filter);
                page = productRepo.findByBrand(pageable, brand.iterator().next());
            }else if(search_by.equals("Price")){
                Double price = Double.parseDouble(filter);
                page = productRepo.findByPrice(pageable, price);
            }else if(search_by.equals("Sale")){
                Integer sale = Integer.parseInt(filter);
                page = productRepo.findBySale(pageable, sale);
            }else if(search_by.equals("Category")){
                Category category = categoryRepo.findByName(filter);
                page = productRepo.findByCategory(pageable, category);
            }else if(search_by.equals("Size")){
                Size size = sizeRepo.findBySize(filter);
                page = productRepo.findBySizes(pageable, size);
            }

        return page;
    }

    public Page<Order> findByFilterOrders(String search_by, String filter, Pageable pageable) {
        Page<Order> page = null;
        if(search_by.equals("Id")) {
            Long id = Long.parseLong(filter);
            page = orderRepo.findById(pageable, id);
        }else if(search_by.equals("Username")){
            User user = userRepo.findByUsername(filter);
            page = orderRepo.findByUser(pageable, user);
        }else if(search_by.equals("Find all")){
            page = orderRepo.findAll(pageable);
        }
        return page;
    }
}
