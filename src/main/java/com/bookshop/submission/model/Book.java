package com.bookshop.submission.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
