package com.bookshop.submission.controller;

import com.bookshop.submission.dto.UserDto;
import com.bookshop.submission.services.UserService;
import jakarta.validation.Valid;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.ui.Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Slf4j
@Controller
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "register_page";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserDto user,
                           BindingResult result,
                           Model model)  throws UnsupportedEncodingException {
        log.info("Registering user {}", user.getUsername());
        if (userService.findByUsername(user.getUsername()) != null) {
            result.rejectValue("username", "username.exists", "Username already exists");
        }
        if (result.hasErrors()) {
            log.info("Validation errors found");
            model.addAttribute("user", user);
            return "register_page";
        }
        log.info("user using 2fa {}", user.isUsing2FA());

        if (user.isUsing2FA()) {
            log.info("Using 2FA");
            user.setSecret(Base32.random());
            model.addAttribute("qr", userService.generateQRUrl(user));
            userService.saveUser(user);
            return "qrcode";
        }
        log.info("saving user {}", user.getUsername());
        userService.saveUser(user);

        return "redirect:/login?reg-success";
    }

    @PostMapping("/user/update/2fa")
    @ResponseBody
    public Map<String, String> modifyUser2FA(@RequestParam("use2FA") boolean use2FA)
            throws UnsupportedEncodingException {
        UserDto user = userService.updateUser2FA(use2FA);
        if (use2FA) {
            return Map.of("message", userService.generateQRUrl(user));
        }
        return Map.of("message", "2FA disabled");
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login_page";
    }

}

