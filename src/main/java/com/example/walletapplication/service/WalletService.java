package com.example.walletapplication.service;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.entity.Wallet;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.repository.UserRepository;
import com.example.walletapplication.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    public WalletService(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void deposit(String username, Double amount) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        Wallet wallet = walletRepository.findByUser(user);
        wallet.deposit(amount);
        walletRepository.save(wallet);
    }

    @Transactional
    public void withdraw(String username, Double amount) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        Wallet wallet = walletRepository.findByUser(user);
        wallet.withdraw(amount);
        walletRepository.save(wallet);
    }
}
