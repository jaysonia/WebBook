package com.bookshop.submission.authentication;

import com.bookshop.submission.model.User;
import com.bookshop.submission.services.LoginRateLimiter;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEventListener {

    private final LoginRateLimiter  loginRateLimiter;

    public  AuthenticationEventListener(LoginRateLimiter loginRateLimiter) {
        this.loginRateLimiter = loginRateLimiter;
    }

    @EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent e){
        String user = (String) e.getAuthentication().getPrincipal();
        loginRateLimiter.loginFailed(user);
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent e){
        User user = (User) e.getAuthentication().getPrincipal();
        loginRateLimiter.loginSuccedded(user.getUsername());
    }
}
