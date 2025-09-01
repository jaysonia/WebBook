package com.bookshop.submission.services;

import com.bookshop.submission.model.CartItems;
import com.bookshop.submission.model.Orders;
import com.bookshop.submission.model.User;

import java.util.List;

public interface OrderService {
    void makeOrder(Orders order, User user);
    List<Orders> getAllOrders(User user);
}
