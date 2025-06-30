package com.bookshop.submission.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name="order_items")
public class OrderItems {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Orders order;

    @ManyToOne
    private Book book;

    private Integer quantity;
    private BigDecimal price;

    //Constructors
    public OrderItems() {
        super();
    }
    public OrderItems(Orders order, Book book, Integer quantity, BigDecimal price) {
        this.order = order;
        this.book = book;
        this.quantity = quantity;
        this.price = price;
    }

    //getters and setters
    public Long getId() {
        return id;
    }
    public Orders getOrder() {
        return order;
    }
    public Book getBook() {
        return book;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
