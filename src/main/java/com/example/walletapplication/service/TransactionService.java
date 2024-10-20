package com.example.walletapplication.service;

import com.example.walletapplication.entity.InterWalletTransaction;
import com.example.walletapplication.entity.IntraWalletTransaction;
import com.example.walletapplication.repository.InterWalletTransactionRepository;
import com.example.walletapplication.repository.IntraWalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private IntraWalletTransactionRepository intraWalletTransactionRepository;

    @Autowired
    private InterWalletTransactionRepository interWalletTransactionRepository;

    public List<Object> getTransactions(Long walletId, LocalDateTime startDate, LocalDateTime endDate) {
        List<IntraWalletTransaction> intraTransactions = intraWalletTransactionRepository.findByWalletIdAndDate(walletId, startDate, endDate);
        List<InterWalletTransaction> senderTransactions = interWalletTransactionRepository.findBySenderWalletIdAndDate(walletId, startDate, endDate);
        List<InterWalletTransaction> receiverTransactions = interWalletTransactionRepository.findByReceiverWalletIdAndDate(walletId, startDate, endDate);

        List<Object> allTransactions = new ArrayList<>();
        allTransactions.addAll(intraTransactions);
        allTransactions.addAll(senderTransactions);
        allTransactions.addAll(receiverTransactions);

        return allTransactions;
    }
}