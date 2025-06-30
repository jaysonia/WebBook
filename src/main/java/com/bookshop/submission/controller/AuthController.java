package com.bookshop.submission.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import com.bookshop.submission.model.Role;
import com.bookshop.submission.model.User;
import com.bookshop.submission.repository.RoleRepository;
import com.bookshop.submission.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register_page";
    }

    @PostMapping("/register")
    public String register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepo.findByName("USER");
        user.getRoles().add(role);
        userRepo.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login_page";
    }

}

