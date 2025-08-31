package com.bookshop.submission.controller;

import com.bookshop.submission.exception.BookNotFoundException;
import com.bookshop.submission.model.Book;
import com.bookshop.submission.services.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("books", bookService.findAllBooks());
        return "index";
    }

    @RequestMapping("/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "addBook";
    }

    @PostMapping("/books")
    //@PreAuthorize("hasRole('ADMIN')")
    public String addBook(@Valid Book book) {
        bookService.addBook(book);
        return "redirect:/";
    }

    @GetMapping("/books/{id}")
    public String getBookById(@PathVariable(value = "id") Long bookId, Model model) {
        try{
            Book book = bookService.findBookById(bookId);
            model.addAttribute("book", book);
            return "editBook";
        } catch (BookNotFoundException e) {
            log.info("tried to access invalid bookid {}", bookId);
            return "redirect:/";
        }
    }

    // Update an Existing Book
    @PutMapping("/save")
    public String updateBook(@ModelAttribute("book") Book book, Model model) {
        try{
            bookService.editBook(book);
        } catch (BookNotFoundException e) {
            log.info("tried to access invalid bookid {}", book.getId());
        }
        return "redirect:/";
    }

    // Delete a Book
    @DeleteMapping("/delete/{id}")
    public String deleteBook(@PathVariable(value = "id") Long bookId, Model model) {
        try{
            bookService.deleteBook(bookId);
        } catch (BookNotFoundException e) {
            log.info("Tried to remove an invalid book with id {}", bookId);
        }

        return "redirect:/";
    }
}

