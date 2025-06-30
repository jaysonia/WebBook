package com.bookshop.submission.exception;

public class BookNotFoundException extends Exception{

    //private long book_id;

    public BookNotFoundException(long book_id) {
        super(String.format("Book not found with id %d", book_id));
    }
}
