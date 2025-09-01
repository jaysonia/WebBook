package com.bookshop.submission.services.impl;

import com.bookshop.submission.services.LoginRateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class LoginRateLimiterImpl implements LoginRateLimiter {
    private static final int MAX_ATTEMPTS = 5;
    private static final long BLOCK_TIME = 15 * 60 * 1000; //15 minutes

    private final Map<String, LoginAttempt> attemptsCache = new ConcurrentHashMap<>();

    public boolean isBlocked(String key){
        LoginAttempt attempt = attemptsCache.get(key);
        if (attempt == null) return false;

        if (attempt.attempts >= MAX_ATTEMPTS) {
            if (Instant.now().toEpochMilli() - attempt.lastAttempt < BLOCK_TIME) {
                log.info("Blocked login attempt for user {} due to repeated failures", key);
                return true;
            } else {
                attemptsCache.remove(key);
            }
        }
        return false;
    }

    public void loginSuccedded(String key){
        attemptsCache.remove(key);
    }

    public void loginFailed(String key){
        log.info("Failed login attempt for user: {}", key);
        LoginAttempt attempt = attemptsCache.getOrDefault(key, new LoginAttempt(0,System.currentTimeMillis()));
        attempt.attempts++;
        attempt.lastAttempt = System.currentTimeMillis();
        attemptsCache.put(key, attempt);
    }

    private static class LoginAttempt {
        int attempts;
        long lastAttempt;

        LoginAttempt(int attempts, long lastAttempt) {
            this.attempts = attempts;
            this.lastAttempt = lastAttempt;
        }
    }
}
