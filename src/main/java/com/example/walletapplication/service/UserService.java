package com.example.walletapplication.service;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.exception.InvalidUsernameAndPasswordException;
import com.example.walletapplication.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User registerUser(String username, String password, CurrencyType currencyType) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            logger.error("Invalid username or password");
            throw new InvalidUsernameAndPasswordException("Username and password cannot be empty");
        }
        User user = new User(username, password, currencyType);
        User savedUser = userRepository.save(user);
        logger.info("User registered successfully: {}", username);
        return savedUser;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", username);
                    return new UsernameNotFoundException("User with username: " + username + " not found");
                });

        logger.info("User found: {}", username);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(() -> "ROLE_USER")
        );
    }
}