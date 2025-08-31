package com.bookshop.submission.services;

import com.bookshop.submission.exception.CartItemNotFoundException;
import com.bookshop.submission.model.Book;
import com.bookshop.submission.model.Cart;
import com.bookshop.submission.model.CartItems;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface CartService {
    Cart getCart(String username);
    List<CartItems> getCartItems(Cart cart);
    void addToCart(Cart cart, Book book, int quantity);
    void removeCartItem(Cart cart, Book book);
    void updateCartItem(long id, int quantity)throws CartItemNotFoundException;
    BigDecimal getCartPrice(List<CartItems> items);
}
