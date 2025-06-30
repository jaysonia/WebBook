package com.bookshop.submission.exception;

public class CartItemNotFoundException extends Exception {
    public CartItemNotFoundException(Long id) {
        super(String.format("Cart item not found with id %d", id));
    }
}
