package com.bookshop.submission.services;

import com.bookshop.submission.dto.UserDto;
import com.bookshop.submission.model.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);
    User findByUsername(String username);
    List<UserDto> findAllUsers();
    String generateQRUrl(UserDto user);
    UserDto updateUser2FA(boolean use2FA);
    UserDto findByUsernameDto(String username);
}
