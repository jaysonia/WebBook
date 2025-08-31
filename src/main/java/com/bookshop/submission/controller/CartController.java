package com.bookshop.submission.controller;

import com.bookshop.submission.dto.UserDto;
import com.bookshop.submission.exception.BookNotFoundException;
import com.bookshop.submission.exception.CartItemNotFoundException;
import com.bookshop.submission.model.*;
import com.bookshop.submission.repository.*;
import com.bookshop.submission.services.BookService;
import com.bookshop.submission.services.CartService;
import com.bookshop.submission.services.UserService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Controller
public class CartController {

    private CartService cartService;
    @Autowired
    private BookService bookService;
    private UserService userService;

    public CartController(CartService cartService,
                          BookService bookService,
                          UserService userService) {
        this.cartService = cartService;
        this.bookService = bookService;
        this.userService = userService;
    }


    @GetMapping("/cart")
    public String home(Model model) {
        User currUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cart cart = cartService.getCart(currUser.getUsername());
        List<CartItems> items = cartService.getCartItems(cart);
        BigDecimal totalPrice = cartService.getCartPrice(items);
        //get user dto from user service
        UserDto user = userService.findByUsernameDto(currUser.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("cartItems", items);
        model.addAttribute("totalPrice", totalPrice);

        return "cart";
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam Long bookId,
                          @RequestParam int quantity) {
        User currUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Adding book {} to {}'s cart", bookId, currUser.getUsername());
        Cart cart = cartService.getCart(currUser.getUsername());
        Book book;
        try{
            book = bookService.findBookById(bookId);
        } catch (BookNotFoundException e) {
            log.error("Unable to locate book with id: " + bookId);
            throw new RuntimeException(e);
        }
        cartService.addToCart(cart, book, quantity);
        return "redirect:/";
    }

    @PostMapping("/updateCartItem/{id}")
    public String updateCartItem(@PathVariable(value = "id") Long id, @RequestParam int quantity) throws CartItemNotFoundException{
        if(quantity > 0) {
            cartService.updateCartItem(id, quantity);
        }
        return "redirect:/cart";
    }

    @DeleteMapping("deleteCartItem/{id}")
    public String deleteCartItem(@PathVariable(value = "id") Long id) throws CartItemNotFoundException {
        User currUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cart cart = cartService.getCart(currUser.getUsername());
        try{
            cartService.removeCartItem(cart, bookService.findBookById(id));
        } catch (BookNotFoundException e) {
            log.error("Unable to locate book with id: " + id);
            throw new CartItemNotFoundException(id);
        }
        return "redirect:/cart";
    }
}
