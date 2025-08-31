package com.bookshop.submission.controller;

import com.bookshop.submission.model.*;
import com.bookshop.submission.repository.*;
import com.bookshop.submission.services.CartService;
import com.bookshop.submission.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping("/order")
    public String order(Model model, BigDecimal totalPrice) {
        User currUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("orders", new Orders(currUser,totalPrice));
        model.addAttribute("total_price", totalPrice);
        return "order";
    }

    @PostMapping("/completeOrder")
    public String completeOrder(@Valid
            @ModelAttribute("orders") Orders order) {
        User currUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderService.makeOrder(order, currUser);
        return "redirect:/?ordered";
    }
}
