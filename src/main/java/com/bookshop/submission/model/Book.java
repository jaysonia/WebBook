package com.bookshop.submission.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Entity
@Table(name="books")
public class Book {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String book_name;

    @NotBlank
    private String author_name;

    @NotBlank
    private String isbn;

    @Min(value=1900)
    private int year;

    @Digits(integer=10, fraction=2)
    @DecimalMin("0.00")
    private BigDecimal price;

    @Min(value=0)
    private int quantity;

    public Book() {
        super();
    }
    public Book(Long id, String book_name, String author_name, String isbn, int year, BigDecimal price, int quantity) {
        this.id = id;
        this.book_name = book_name;
        this.author_name = author_name;
        this.isbn = isbn;
        this.price = price;
        this.year = year;
        this.quantity = quantity;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getAuthor_name() {
        return author_name;
    }
    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
