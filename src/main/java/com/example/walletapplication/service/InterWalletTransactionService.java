package com.example.walletapplication.service;

import com.example.walletapplication.entity.InterWalletTransaction;
import com.example.walletapplication.entity.User;
import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.enums.TransactionType;
import com.example.walletapplication.exception.UserAndWalletMismatchException;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.exception.WalletNotFoundException;
import com.example.walletapplication.repository.InterWalletTransactionRepository;
import com.example.walletapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class InterWalletTransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InterWalletTransactionRepository interWalletTransactionRepository;

    @Transactional
    public void transfer(Long senderId, Long receiverId, Double amount, CurrencyType currency) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UserNotFoundException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new UserNotFoundException("Receiver not found"));

        if (sender.getWallet() == null) {
            throw new WalletNotFoundException("Wallet not found");
        }

        sender.getWallet().withdraw(amount, currency);
        receiver.getWallet().deposit(amount, currency);

        InterWalletTransaction transaction = new InterWalletTransaction(sender.getWallet(), receiver.getWallet(), TransactionType.TRANSFER, amount, LocalDateTime.now());
        interWalletTransactionRepository.save(transaction);
    }
}