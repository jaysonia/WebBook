package com.bookshop.submission.init;

import com.bookshop.submission.model.Book;
import com.bookshop.submission.model.Role;
import com.bookshop.submission.model.User;
import com.bookshop.submission.repository.BookRepository;
import com.bookshop.submission.repository.RoleRepository;
import com.bookshop.submission.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepo;
    @Autowired
    BookRepository bookRepo;
    @Autowired
    PasswordEncoder passwordEncoder;

    public void run(String... args) throws Exception {
        if(roleRepository.findByName("ADMIN")==null){
            System.out.println("Initialising roles");
            Role role = new Role();
            role.setName("ADMIN");
            roleRepository.save(role);
            Role user = new Role();
            user.setName("USER");
            roleRepository.save(user);
        }

        if(userRepo.findByUsername("admin")==null){
            System.out.println("Adding admin user to database");
            User user = new User();
            user.setUsername("admin");
            String password = PasswordGenerator(15);
            System.out.println("Admin Password is: "+password);
            user.setPassword(passwordEncoder.encode(password));
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName("ADMIN"));
            user.setRoles(roles);
            userRepo.save(user);
        }

        Book book = new Book();
        book.setIsbn("123456789");
        book.setBook_name("Book 1");
        book.setAuthor_name("Author 1");
        book.setYear(1967);
        book.setPrice(new BigDecimal("42.83"));
        book.setQuantity(2);
        bookRepo.save(book);

        Book book2 = new Book();
        book2.setIsbn("987654321");
        book2.setBook_name("Book 2");
        book2.setAuthor_name("Author 2");
        book2.setYear(1967);
        book2.setPrice(new BigDecimal("25.67"));
        book2.setQuantity(5);
        bookRepo.save(book2);

    }

    private String PasswordGenerator(int length) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=<>?";
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }
}
