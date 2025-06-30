package com.bookshop.submission.model;

import jakarta.persistence.*;

@Entity
public class Role {
    @Id @GeneratedValue
    private Long id;

    private String name;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

