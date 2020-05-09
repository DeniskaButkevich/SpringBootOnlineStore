package com.dez.predesign.controller.admin;

import com.dez.predesign.data.Order;
import com.dez.predesign.repository.OrderRepo;
import com.dez.predesign.service.OrderService;
import com.dez.predesign.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admins/orders")
public class OrdersController {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    OrderService orderService;

    @Autowired
    PageService pageService;

    @GetMapping()
    public String getOrders(Model model,
                            @PageableDefault(sort = {"id"},direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                            @RequestParam(required = false, defaultValue = "") String filter,
                            @RequestParam(required = false) String search_by){
        Page<Order> page;
        if(filter != null && !filter.isEmpty() && search_by != null){
            page = pageService.findByFilterOrders(search_by, filter, pageable);
        } else {
            page = orderRepo.findAll(pageable);
        }
        List<Integer> listpages = pageService.listPages(page);
        model.addAttribute("listpages", listpages);
        model.addAttribute("url", "/admins/orders");
        model.addAttribute("page", page);

        model.addAttribute("filter", filter);
        model.addAttribute("search_by", search_by);
        return "admins/orderList";
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable Long id,Model model){
        Order order = orderRepo.findById(id).get();
        model.addAttribute("order",order);
        return "admins/order";
    }

    @PostMapping("/{id}")
    public String updateStatus(@RequestParam Integer active, @PathVariable Long id){

        Order order = orderRepo.findById(id).get();
        order.setActive(active);
        orderRepo.save(order);

        return "redirect:/admins/orders/{id}";
    }
}
