package com.bookshop.submission.services;

import com.bookshop.submission.exception.BookNotFoundException;
import com.bookshop.submission.model.Book;

import java.math.BigDecimal;
import java.util.List;

public interface BookService {

    void editBook(Book book)throws BookNotFoundException;
    void deleteBook(long id) throws BookNotFoundException;
    void addBook(Book book);
    void updateBookQuantity(long id, int quantity) throws BookNotFoundException;
    Book findBookById(long id) throws BookNotFoundException;
    List<Book> findAllBooks();
    BigDecimal findBookPriceById(long id) throws BookNotFoundException;
}
