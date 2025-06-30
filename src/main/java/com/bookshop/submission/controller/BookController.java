package com.bookshop.submission.controller;

import com.bookshop.submission.exception.BookNotFoundException;
import com.bookshop.submission.repository.BookRepository;
import com.bookshop.submission.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepo;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("books", bookRepo.findAll());
        return "index";
    }

    @RequestMapping("/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "addBook";
    }

    @PostMapping("/books")
    public String addBook(Book book) {
        bookRepo.save(book);
        return "redirect:/";
    }

    @GetMapping("/books/{id}")
    public String getBookById(@PathVariable(value = "id") Long bookId, Model model) throws BookNotFoundException {
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
        model.addAttribute("book", book);
        return "editBook";
    }

    // Update an Existing Book
    @PutMapping("/save")
    public String updateBook(@ModelAttribute("book") Book book, Model model)
            throws BookNotFoundException{
        bookRepo.save(book);
        return "redirect:/";
    }

    // Delete a Book
    @DeleteMapping("/delete/{id}")
    public String deleteBook(@PathVariable(value = "id") Long bookId, Model model) throws BookNotFoundException {
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
        bookRepo.delete(book);
        return "redirect:/";
    }
}

