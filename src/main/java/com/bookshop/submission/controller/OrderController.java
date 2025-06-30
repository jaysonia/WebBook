package com.bookshop.submission.controller;

import com.bookshop.submission.model.*;
import com.bookshop.submission.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.xml.catalog.Catalog;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private CartItemsRepository cartItemsRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private OrdersRepository ordersRepo;
    @Autowired
    private OrderItemsRepository orderItemsRepos;

    @RequestMapping("/order")
    public String order(Model model, Principal principal, BigDecimal totalPrice) {
        model.addAttribute("orders", new Orders());
        model.addAttribute("total_price", totalPrice);
        return "order";
    }

    @PostMapping("/completeOrder")
    public String completeOrder(
            @ModelAttribute("orders") Orders order,
            Model model,
            Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        Cart cart = cartRepo.findByUserId(user.getId());
        List<CartItems> items = cartItemsRepo.findByCart(cart);
        order.setUser(user);
        order.setOrderDate();
        order.setStatus("ordered");
        ordersRepo.save(order);
        for (CartItems item : items) {
            orderItemsRepos.save(new OrderItems(
                    order,
                    item.getBook(),
                    item.getQuantity(),
                    item.getBook().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))));
            //reduce stock quantity
            item.getBook().setQuantity(item.getBook().getQuantity() - item.getQuantity());
            //remove item from cart
            cartItemsRepo.delete(item);
        }
        return "redirect:/?ordered";
    }
}
