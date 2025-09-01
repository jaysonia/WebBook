package com.bookshop.submission.authentication;

import com.bookshop.submission.model.User;
import com.bookshop.submission.repository.UserRepository;
import com.bookshop.submission.services.LoginRateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.jboss.aerogear.security.otp.Totp;

@Slf4j
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginRateLimiter loginRateLimiter;


    @Override
    public Authentication authenticate(Authentication auth)
            throws AuthenticationException {
        String verificationCode
                = ((CustomWebAuthenticationDetails) auth.getDetails())
                .getVerificationCode();


        User user = userRepository.findByUsername(auth.getName());
        if (loginRateLimiter.isBlocked(user.getUsername())) {
            throw new BadCredentialsException("Too many log attempts, please try again");
        }

        if ((user == null)) {
            log.warn("User tried to login with invalid credentials, {}",auth.getName());
            throw new BadCredentialsException("Invalid username or password");
        }
        if (user.isUsing2FA()) {

            Totp totp = new Totp(user.getSecret());
            if (!isValidLong(verificationCode) || !totp.verify(verificationCode)) {
                throw new BadCredentialsException("Invalid verfication code");
            }
        }

        Authentication result = super.authenticate(auth);

        log.info("User, logged in with Credentials {}", result.getAuthorities().toString());

        return new UsernamePasswordAuthenticationToken(
                user, result.getCredentials(), result.getAuthorities());
    }

    private boolean isValidLong(String code) {
        try {
            Long.parseLong(code);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


}

