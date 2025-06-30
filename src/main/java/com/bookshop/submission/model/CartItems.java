package com.bookshop.submission.model;

import jakarta.persistence.*;

@Entity
@Table(name="cart_items")
public class CartItems {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Cart cart;

    @ManyToOne
    private Book book;

    private Integer quantity;

    //Constructors
    public CartItems() {
        super();
    }
    public CartItems(Cart cart, Book book, Integer quantity) {
        this.cart = cart;
        this.book = book;
        this.quantity = quantity;
    }

    //Getters and Setters

    public Long getId() {
        return id;
    }
    public Cart getCart() {
        return cart;
    }
    public void setCart(Cart cart) {
        this.cart = cart;
    }
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
