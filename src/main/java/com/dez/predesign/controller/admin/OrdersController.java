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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admins/")
public class OrdersController {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    OrderService orderService;

    @Autowired
    PageService pageService;

    @GetMapping("orders")
    public String orderList(Model model, @PageableDefault(sort = {"id"},direction = Sort.Direction.DESC, size = 5) Pageable pageable){

        Page<Order> page;

        page = orderRepo.findAll(pageable);
        model.addAttribute("page", page);

        List<Integer> listpages = pageService.listPages(page);
        model.addAttribute("listpages", listpages);

        model.addAttribute("url", "/admins/orders");
        model.addAttribute("page", page);
        return "/admins/orderList";
    }

    @GetMapping("order/delete/{id}")
    public String orderRemove(@PathVariable Long id){
        Order order = orderRepo.findById(id).get();
        orderRepo.delete(order);
        return "redirect:/admins/orders";
    }
}
