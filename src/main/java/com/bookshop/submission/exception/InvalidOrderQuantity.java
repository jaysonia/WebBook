package com.bookshop.submission.exception;

public class InvalidOrderQuantity extends RuntimeException {
    public InvalidOrderQuantity(String message) {
        super(message);
    }
}
