package com.example.walletapplication.service;

import com.example.walletapplication.entity.IntraTransaction;
import com.example.walletapplication.entity.User;
import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.enums.TransactionType;
import com.example.walletapplication.exception.*;
import com.example.walletapplication.repository.IntraTransactionRepository;
import com.example.walletapplication.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class WalletService {

    private final UserRepository userRepository;
    private final IntraTransactionRepository intraTransactionRepository;

    public WalletService(UserRepository userRepository, IntraTransactionRepository intraTransactionRepository) {
        this.userRepository = userRepository;
        this.intraTransactionRepository = intraTransactionRepository;
    }

    public void isUserAuthorized(Long userId, Long walletId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = authentication.getName();
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!user.getUsername().equals(authenticatedUsername)){
            throw new UnAuthorisedUserException("User not authorized");
        }
        if(!user.getWallet().getId().equals(walletId)) {
            throw new UnAuthorisedWalletException("User not authorized for this Wallet");
        }
    }

    @Transactional
    public void deposit(Long userId, Double amount, CurrencyType depositCurrency) throws InvalidAmountException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.getWallet().deposit(amount, depositCurrency);

        IntraTransaction transaction = new IntraTransaction(user.getWallet(), TransactionType.DEPOSIT, amount, LocalDateTime.now());
        intraTransactionRepository.save(transaction);
    }

    @Transactional
    public Double withdraw(Long userId, Double amount, CurrencyType withdrawalCurrency) throws InsufficientBalanceException{
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.getWallet().withdraw(amount, withdrawalCurrency);

        IntraTransaction transaction = new IntraTransaction(user.getWallet(), TransactionType.WITHDRAWAL, amount, LocalDateTime.now());
        intraTransactionRepository.save(transaction);
        return amount;
    }
}
