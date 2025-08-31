package com.bookshop.submission.services.impl;

import com.bookshop.submission.exception.BookNotFoundException;
import com.bookshop.submission.exception.InvalidOrderQuantity;
import com.bookshop.submission.model.Book;
import com.bookshop.submission.repository.BookRepository;
import com.bookshop.submission.services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    BookRepository bookRepo;
    public BookServiceImpl(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Override
    public void editBook(Book book) throws BookNotFoundException {
        Book storedbook = bookRepo.findById(book.getId()).orElseThrow(() -> new BookNotFoundException(book.getId()));
        storedbook.setBook_name(book.getBook_name());
        storedbook.setIsbn(book.getIsbn());
        storedbook.setPrice(book.getPrice());
        storedbook.setAuthor_name(book.getAuthor_name());
        storedbook.setQuantity(book.getQuantity());
        storedbook.setYear(book.getYear());
        bookRepo.save(book);
    }

    public void updateBookQuantity(long id, int quantity) throws BookNotFoundException {
        Book storedbook = bookRepo.findById(id);
        if(storedbook == null)
            throw new BookNotFoundException(id);
        int value = storedbook.getQuantity() - quantity;
        if (value < 0) {
            throw new InvalidOrderQuantity("Cannot update book quantity less than 0");
        } else {
            storedbook.setQuantity(value);
            bookRepo.save(storedbook);
        }
    }

    public void deleteBook(long id) throws BookNotFoundException {
        Book book = bookRepo.findById(id);
        if (book != null) {
            bookRepo.delete(book);
        }
        else {
            throw new BookNotFoundException(id);
        }
    }

    public void addBook(Book book){
        bookRepo.save(book);
    }

    public Book findBookById(long id) throws BookNotFoundException {
        Book book = bookRepo.findById(id);
        if (book != null) {
            return bookRepo.findById(id);
        }
        else {
            throw new BookNotFoundException(id);
        }
    }

    public List<Book> findAllBooks() {
        return bookRepo.findAll();
    }

    public BigDecimal findBookPriceById(long id) throws BookNotFoundException {
        Book book = findBookById(id);
        return book.getPrice();
    }

}
