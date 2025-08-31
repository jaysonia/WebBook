package com.bookshop.submission.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotEmpty
    private String username;
    @NotEmpty(message = "Password Cannot be empty")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$",
            message = "Password must contain 8 letters")
    private String password;
    private String secret;
    private boolean isUsing2FA;
    @NotEmpty(message = "Email Should not be empty")
    @Email
    private String email;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Past
    private LocalDate dob;
    private String phone;
    private String address;
    private String name;
    private String surname;
}
