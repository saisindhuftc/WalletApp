package com.example.walletapplication.service;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.exception.UserAlreadyExistsException;
import com.example.walletapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String password) {
        if (userRepository.findByUsername(username) != null) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        String encodedPassword = passwordEncoder.encode(password);
        User newUser = new User(username, encodedPassword);
        userRepository.save(newUser);
        return newUser;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
