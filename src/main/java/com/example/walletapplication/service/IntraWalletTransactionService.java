package com.example.walletapplication.service;

import com.example.walletapplication.entity.IntraWalletTransaction;
import com.example.walletapplication.entity.User;
import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.enums.TransactionType;
import com.example.walletapplication.exception.*;
import com.example.walletapplication.repository.IntraWalletTransactionRepository;
import com.example.walletapplication.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IntraWalletTransactionService {

    private final UserRepository userRepository;
    private final IntraWalletTransactionRepository intraTransactionRepository;

    public IntraWalletTransactionService(UserRepository userRepository, IntraWalletTransactionRepository intraTransactionRepository) {
        this.userRepository = userRepository;
        this.intraTransactionRepository = intraTransactionRepository;
    }

    public void isUserAuthorized(Long userId, Long walletId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = authentication.getName();
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!user.getUsername().equals(authenticatedUsername)) {
            throw new UnAuthorisedUserException("User not authorized");
        }
        if (!user.getWallet().getId().equals(walletId)) {
            throw new UnAuthorisedWalletException("User not authorized for this Wallet");
        }
    }

    @Transactional
    public void deposit(Long userId, Double amount, CurrencyType depositCurrency) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than zero.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.getWallet().deposit(amount, depositCurrency);

        IntraWalletTransaction transaction = new IntraWalletTransaction(user.getWallet(), TransactionType.DEPOSIT, amount, LocalDateTime.now());
        intraTransactionRepository.save(transaction);
    }

    @Transactional
    public Double withdraw(Long userId, Double amount, CurrencyType withdrawalCurrency) throws InsufficientBalanceException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.getWallet().withdraw(amount, withdrawalCurrency);

        IntraWalletTransaction transaction = new IntraWalletTransaction(user.getWallet(), TransactionType.WITHDRAWAL, amount, LocalDateTime.now());
        intraTransactionRepository.save(transaction);
        return amount;
    }

    public List<IntraWalletTransaction> getDeposits(Long walletId) {
        return intraTransactionRepository.findByWalletIdAndType(walletId, TransactionType.DEPOSIT);
    }

    public List<IntraWalletTransaction> getWithdrawals(Long walletId) {
        return intraTransactionRepository.findByWalletIdAndType(walletId, TransactionType.WITHDRAWAL);
    }
}
