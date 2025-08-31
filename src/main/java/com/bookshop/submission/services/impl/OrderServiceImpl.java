package com.bookshop.submission.services.impl;

import com.bookshop.submission.exception.BookNotFoundException;
import com.bookshop.submission.model.*;
import com.bookshop.submission.repository.OrderItemsRepository;
import com.bookshop.submission.repository.OrdersRepository;
import com.bookshop.submission.services.BookService;
import com.bookshop.submission.services.CartService;
import com.bookshop.submission.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private OrdersRepository ordersRepository;
    private OrderItemsRepository orderItemsRepository;
    private CartService cartService;
    private BookService bookService;

    @Autowired
    public OrderServiceImpl(OrdersRepository ordersRepository,
                            CartService cartService,
                            BookService bookService,
                            OrderItemsRepository orderItemsRepository) {
        this.ordersRepository = ordersRepository;
        this.cartService = cartService;
        this.bookService = bookService;
        this.orderItemsRepository = orderItemsRepository;
    }

    public void makeOrder(Orders order, User user) {
        Cart cart = cartService.getCart(user.getUsername());
        List<CartItems> items = cartService.getCartItems(cart);
        order.setUser(user);
        order.setOrderDate();
        order.setStatus("ordered");
        order.settotalAmount(cartService.getCartPrice(items));
        ordersRepository.save(order);
        for (CartItems item : items) {
            try{
                bookService.updateBookQuantity(item.getBook().getId(),item.getQuantity());
            } catch (BookNotFoundException e) {
                log.info(e.getMessage());
                throw new RuntimeException(e);
            }
            orderItemsRepository.save(new OrderItems(
                    order,
                    item.getBook(),
                    item.getQuantity(),
                    item.getBook().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
            ));
            cartService.removeCartItem(item.getCart(),item.getBook());
        }
    }

    public List<Orders> getAllOrders(User user) {
        return ordersRepository.findAllByUserId(user.getId());
    }
}
