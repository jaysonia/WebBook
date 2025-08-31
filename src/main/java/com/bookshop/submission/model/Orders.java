package com.bookshop.submission.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="orders")
public class Orders {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private String status;
    private String shippingAddress;
    private int creditCard;
    private int expiry;
    private int cvv;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItems> orderItems;

    //Constructors
    public Orders() {
        super();
    }

    public Orders(User user,
                  BigDecimal totalAmount) {
        this.user = user;
        this.totalAmount = totalAmount;
        this.creditCard = 0;
        this.expiry = 0;
        this.cvv = 0;
    }

    public Orders(User user,
                  BigDecimal totalAmount,
                  String shippingAddress,
                  int creditCard,
                  int expiry,
                  int cvv) {
        this.user = user;
        this.orderDate = LocalDateTime.now();
        this.totalAmount = totalAmount;
        this.status = "ordered";
        this.shippingAddress = shippingAddress;
    }

    //getter and Setters
    public Long getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    public void setOrderDate() {
        this.orderDate = LocalDateTime.now();
    }
    public BigDecimal gettotalAmount() {
        return totalAmount;
    }
    public void settotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getShippingAddress() {
        return shippingAddress;
    }
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    public List<OrderItems> getOrderItems() {
        return orderItems;
    }

    public void setCreditCard(int creditCard) {
        this.creditCard = creditCard;
    }
    public int getCreditCard() {
        return creditCard;
    }
    public void setExpiry(int expiry) {
        this.expiry = expiry;
    }
    public int getExpiry() {
        return expiry;
    }
    public void setCvv(int cvv) {
        this.cvv = cvv;
    }
    public int getCvv() {
        return cvv;
    }

}
