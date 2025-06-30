package com.bookshop.submission.repository;

import com.bookshop.submission.model.Book;
import com.bookshop.submission.model.Cart;
import com.bookshop.submission.model.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemsRepository extends JpaRepository<CartItems, Long> {
    List<CartItems> findByCart(Cart cart);
    CartItems findByBook(Book book);
}
