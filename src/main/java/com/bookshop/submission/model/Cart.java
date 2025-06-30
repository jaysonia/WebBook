package com.bookshop.submission.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="cart")
public class Cart {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItems> items;

    public Cart() {
        super();
    }

    public Cart(User user) {
        this.user = user;
    }

    //Getters and Setters
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public List<CartItems> getItems() {
        return items;
    }
    public void setItems(List<CartItems> items) {
        this.items = items;
    }
}
