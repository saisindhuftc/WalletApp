package com.example.walletapplication.service;

import com.example.walletapplication.entity.Transaction;
import com.example.walletapplication.entity.User;
import com.example.walletapplication.entity.Wallet;
import com.example.walletapplication.enums.IntraTransactionType;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.repository.TransactionRepository;
import com.example.walletapplication.repository.UserRepository;
import com.example.walletapplication.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WalletService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public void deposit(Long userId, Double amount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Wallet wallet = walletRepository.findByUser(user);
        wallet.deposit(amount);
        walletRepository.save(wallet);

        Transaction transaction = new Transaction(amount, IntraTransactionType.DEPOSIT,wallet.getWalletId());
        transactionRepository.save(transaction);
    }

    @Transactional
    public void withdraw(Long userId, Double amount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Wallet wallet = walletRepository.findByUser(user);
        wallet.withdraw(amount);
        walletRepository.save(wallet);

        Transaction transaction = new Transaction(amount, IntraTransactionType.WITHDRAWAL,wallet.getWalletId());
        transactionRepository.save(transaction);
    }

    @Transactional
    public void transfer(Long fromUserId, Long toUserId, Double amount) {
        User fromUser = userRepository.findById(fromUserId).orElseThrow(() -> new UserNotFoundException("Sender not found"));
        User toUser = userRepository.findById(toUserId).orElseThrow(() -> new UserNotFoundException("Receiver not found"));

        Wallet fromWallet = walletRepository.findByUser(fromUser);
        Wallet toWallet = walletRepository.findByUser(toUser);

        fromWallet.transfer(amount, toWallet);

        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);
    }

    public List<Transaction> getTransactionHistory(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Wallet wallet = walletRepository.findByUser(user);
        return transactionRepository.findByWalletId(wallet.getWalletId());
    }
}
