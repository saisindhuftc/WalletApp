package com.example.walletapplication.service;

import com.example.walletapplication.entity.InterTransaction;
import com.example.walletapplication.entity.IntraTransaction;
import com.example.walletapplication.entity.User;
import com.example.walletapplication.enums.TransactionType;
import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.repository.InterTransactionRepository;
import com.example.walletapplication.repository.IntraTransactionRepository;
import com.example.walletapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InterTransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InterTransactionRepository interTransactionRepository;

    @Autowired
    private IntraTransactionRepository intraTransactionRepository;

    @Transactional
    public void transfer(Long senderId, Long receiverId, Double amount, CurrencyType currency) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UserNotFoundException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new UserNotFoundException("Receiver not found"));

        try {
            sender.getWallet().withdraw(amount, currency);
            receiver.getWallet().deposit(amount, currency);

            InterTransaction transaction = new InterTransaction(sender.getWallet(), receiver.getWallet(),TransactionType.TRANSFER, amount, LocalDateTime.now());
            interTransactionRepository.save(transaction);
        } catch (InsufficientBalanceException e) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
    }

    public List<Object> getTransactionHistory(Long walletId, LocalDateTime startDate, LocalDateTime endDate) {
        List<IntraTransaction> intraTransactions = intraTransactionRepository.findByWalletIdAndDate(walletId,startDate, endDate);
        List<InterTransaction> senderTransactions = interTransactionRepository.findBySenderWalletIdAndDate(walletId, startDate, endDate);
        List<InterTransaction> receiverTransactions = interTransactionRepository.findByReceiverWalletIdAndDate(walletId, startDate, endDate);

        List<Object> allTransactions = new ArrayList<>();
        allTransactions.addAll(intraTransactions);
        allTransactions.addAll(senderTransactions);
        allTransactions.addAll(receiverTransactions);

        return allTransactions;
    }

    public List<IntraTransaction> getTransactionHistoryByType(Long walletId, TransactionType type) {
        return intraTransactionRepository.findByWalletId(walletId, type);
    }
}