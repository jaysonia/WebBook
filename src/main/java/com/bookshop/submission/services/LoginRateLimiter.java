package com.bookshop.submission.services;

public interface LoginRateLimiter {
    boolean isBlocked(String key);
    void loginSuccedded(String key);
    void loginFailed(String key);
}
