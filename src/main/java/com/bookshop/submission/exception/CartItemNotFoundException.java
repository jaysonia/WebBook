package com.bookshop.submission.exception;

public class CartItemNotFoundException extends Exception {
    public CartItemNotFoundException(Long id) {
        super("Cart item not found");
    }
}
