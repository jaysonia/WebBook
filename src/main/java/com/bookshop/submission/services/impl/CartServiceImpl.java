package com.bookshop.submission.services.impl;

import com.bookshop.submission.exception.CartItemNotFoundException;
import com.bookshop.submission.model.Book;
import com.bookshop.submission.model.Cart;
import com.bookshop.submission.model.CartItems;
import com.bookshop.submission.model.User;
import com.bookshop.submission.repository.CartItemsRepository;
import com.bookshop.submission.repository.CartRepository;
import com.bookshop.submission.services.CartService;
import com.bookshop.submission.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

    private CartRepository cartRepository;
    private UserService userService;
    private CartItemsRepository cartItemsRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           UserService userService,
                           CartItemsRepository cartItemsRepository) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.cartItemsRepository = cartItemsRepository;
    }

    public Cart getCart(String username) {
        User user = userService.findByUsername(username);
        log.info("getCart username = {}", username);
        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null) {
            cart = cartRepository.save(new Cart(user));
        }
        return cart;
    }

    public List<CartItems> getCartItems(Cart cart) {
        return cartItemsRepository.findByCart(cart);
    }

    public BigDecimal getCartPrice(List<CartItems> items){
        BigDecimal totalPrice = new BigDecimal(0);
        if (items != null){
            for (CartItems item: items){
                totalPrice = totalPrice.add(item.getBook().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }
        return totalPrice;
    }

    public void addToCart(Cart cart, Book book, int quantity){
        CartItems item = cartItemsRepository.findByCartAndBook(cart, book);
        if (item != null){
            item.setQuantity(item.getQuantity() + quantity);
            //ensure cart can't be higher than available books
            if (item.getQuantity() > item.getBook().getQuantity()){
                item.setQuantity(item.getBook().getQuantity());
            }
        } else {
            item = new CartItems(cart, book, quantity);
        }
        cartItemsRepository.save(item);
    }

    public void removeCartItem(Cart cart, Book book){
        CartItems item = cartItemsRepository.findByCartAndBook(cart, book);
        if (item != null){
            cartItemsRepository.delete(item);
        } else {
            log.info("Unable to locate {} in the cart", book.getBook_name());
        }
    }

    public void updateCartItem(long id, int quantity) throws CartItemNotFoundException{
        CartItems item = cartItemsRepository.findById(id).orElseThrow(() -> new CartItemNotFoundException(id));
        item.setQuantity(quantity);
        if (item.getQuantity() > item.getBook().getQuantity()){
            item.setQuantity(item.getBook().getQuantity());
        }
        cartItemsRepository.save(item);
    }
}
