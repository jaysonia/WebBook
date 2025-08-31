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
        log.info("Editing book with id: {}", book.getId());
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
        log.info("Updating Book Quantity for Book with id: {}", id);
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
        log.info("Deleting a book with id: {}", id);
        Book book = bookRepo.findById(id);
        if (book != null) {
            bookRepo.delete(book);
        }
        else {
            throw new BookNotFoundException(id);
        }
    }

    public void addBook(Book book){
        log.info("Adding a new book with name: {}", book.getBook_name());
        bookRepo.save(book);
    }

    public Book findBookById(long id) throws BookNotFoundException {
        log.info("Finding a book with id: {}", id);
        Book book = bookRepo.findById(id);
        if (book != null) {
            return bookRepo.findById(id);
        }
        else {
            throw new BookNotFoundException(id);
        }
    }

    public List<Book> findAllBooks() {
        log.info("Finding all books");
        return bookRepo.findAll();
    }

    public BigDecimal findBookPriceById(long id) throws BookNotFoundException {
        log.info("Finding a book with id: {}", id);
        Book book = findBookById(id);
        return book.getPrice();
    }

}
