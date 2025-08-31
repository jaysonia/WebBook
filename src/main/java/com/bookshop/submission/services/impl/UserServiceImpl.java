package com.bookshop.submission.services.impl;

import com.bookshop.submission.dto.UserDto;
import com.bookshop.submission.model.User;
import com.bookshop.submission.model.Role;
import com.bookshop.submission.repository.RoleRepository;
import com.bookshop.submission.repository.UserRepository;
import com.bookshop.submission.services.UserService;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setDob(userDto.getDob());
        user.setAddress(userDto.getAddress());
        user.setPhone(userDto.getPhone());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("USER"));
        user.setRoles(roles);
        if (userDto.isUsing2FA()) {
            user.setSecret(userDto.getSecret());
            user.setUsing2FA(userDto.isUsing2FA());
        }

        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public String generateQRUrl(UserDto user){
        String issuer = "BookShop"; // Shown in Authenticator
        String label  = user.getUsername();
        // Construct otpauth URI
        String otpauth = "otpauth://totp/"
                + URLEncoder.encode(issuer + ":" + label, StandardCharsets.UTF_8)
                + "?secret=" + URLEncoder.encode(user.getSecret(), StandardCharsets.UTF_8)
                + "&issuer=" + URLEncoder.encode(issuer, StandardCharsets.UTF_8);

        // Use QuickChart instead of deprecated Google Charts
        return "https://quickchart.io/qr?size=200&text=" +
                URLEncoder.encode(otpauth, StandardCharsets.UTF_8);
    }

    public UserDto findByUsernameDto(String username){
        User user = userRepository.findByUsername(username);
        return convertEntityToDto(user);
    }

    @Override
    public UserDto updateUser2FA(boolean use2FA){
        Authentication curAuth = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) curAuth.getPrincipal();

        currentUser.setUsing2FA(use2FA);
        currentUser.setSecret(Base32.random());

        currentUser = userRepository.save(currentUser);
        return convertEntityToDto(currentUser);
    }

    private UserDto convertEntityToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setSurname(user.getSurname());
        userDto.setDob(user.getDob());
        userDto.setSecret(user.getSecret());
        userDto.setUsing2FA(user.isUsing2FA());

        return userDto;
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }
}
