package com.bookshop.submission.controller;

import com.bookshop.submission.exception.BookNotFoundException;
import com.bookshop.submission.exception.CartItemNotFoundException;
import com.bookshop.submission.model.*;
import com.bookshop.submission.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private BookRepository bookRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CartItemsRepository cartItemsRepository;


    @GetMapping("/cart")
    public String home(Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        Cart cart = cartRepo.findByUserId(user.getId());
        if  (cart == null) {
            cartRepo.save(new Cart(user));
        }
        List<CartItems> items = cartItemsRepository.findByCart(cart);
        BigDecimal totalPrice = new BigDecimal(0);
        for (CartItems item : items) {
            totalPrice = totalPrice.add(item.getBook().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        model.addAttribute("user", user);
        model.addAttribute("cartItems", items);
        model.addAttribute("totalPrice", totalPrice);

        return "cart";
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam Long bookId,
                          @RequestParam int quantity,
                          Principal principal) throws BookNotFoundException {
        User user = userRepo.findByUsername(principal.getName());
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
        if (cartRepo.findByUserId(user.getId()) == null){
            cartRepo.save(new Cart(user));
        }
        CartItems cartItems = cartItemsRepository.findByBook(book);
        if (cartItems == null){
            cartItems = new CartItems(cartRepo.findByUserId(user.getId()), book, quantity);
        } else {
            cartItems.setQuantity(cartItems.getQuantity() + quantity);
            //ensure cart can't be higher than available books
            if (cartItems.getQuantity() > cartItems.getBook().getQuantity()){
                cartItems.setQuantity(cartItems.getBook().getQuantity());
            }
        }

        cartItemsRepository.save(cartItems);
        return "redirect:/";
    }

    @PostMapping("/updateCartItem/{id}")
    public String updateCartItem(@PathVariable(value = "id") Long id, @RequestParam int quantity) throws CartItemNotFoundException{
        CartItems cartItem = cartItemsRepository.findById(id).orElseThrow(() -> new CartItemNotFoundException(id));
        cartItem.setQuantity(quantity);
        if (cartItem.getQuantity() > cartItem.getBook().getQuantity()){
            cartItem.setQuantity(cartItem.getBook().getQuantity());
        }
        cartItemsRepository.save(cartItem);
        return "redirect:/cart";
    }

    @DeleteMapping("deleteCartItem/{id}")
    public String deleteCartItem(@PathVariable(value = "id") Long id) throws CartItemNotFoundException {
        CartItems cartItem = cartItemsRepository.findById(id).orElseThrow(() -> new CartItemNotFoundException(id));
        cartItemsRepository.delete(cartItem);
        return "redirect:/cart";
    }
}
