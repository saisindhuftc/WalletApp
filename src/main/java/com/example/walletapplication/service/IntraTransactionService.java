package com.example.walletapplication.service;

import com.example.walletapplication.entity.InterTransaction;
import com.example.walletapplication.entity.IntraTransaction;
import com.example.walletapplication.entity.User;
import com.example.walletapplication.entity.Wallet;
import com.example.walletapplication.repository.InterTransactionRepository;
import com.example.walletapplication.repository.IntraTransactionRepository;
import com.example.walletapplication.repository.UserRepository;
import com.example.walletapplication.responseModels.InterTransactionResponseModel;
import com.example.walletapplication.responseModels.TransactionsResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class IntraTransactionService{

    @Autowired
    private InterTransactionRepository interTransactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IntraTransactionRepository intraTransactionRepository;

    public TransactionsResponseModel allTransactions() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Username not found."));
        List<Integer> userWallets = user.getWallets().stream().map(Wallet::getWalletId).toList();

        List<IntraTransaction> intraWalletTransactions = intraTransactionRepository.findByWallets(userWallets);
        List<InterTransaction> interWalletTransactions = interTransactionRepository.findTransactionsOfUser(user);
        for(InterTransaction interWalletTransaction : interWalletTransactions){
            intraWalletTransactions.remove(interWalletTransaction.getDeposit());
            intraWalletTransactions.remove(interWalletTransaction.getWithdrawal());
        }
        List<InterTransactionResponseModel> interWalletTransactionResponseModels = interWalletTransactions.stream().map((transaction -> new InterTransactionResponseModel(transaction.getInterWalletTransactionId(), transaction.getSender().getUsername(), transaction.getSenderWalletId(), transaction.getReceiver().getUsername(), transaction.getReceiverWalletId(), transaction.getDeposit(), transaction.getWithdrawal()))).collect(Collectors.toList());

        return new TransactionsResponseModel(interWalletTransactionResponseModels, intraWalletTransactions);
    }

    public TransactionsResponseModel allTransactionsDateBased(LocalDate startDate, LocalDate endDate) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Username not found."));
        List<Integer> userWallets = user.getWallets().stream().map(Wallet::getWalletId).toList();

        List<InterTransaction> interWalletTransactions = interTransactionRepository.findTransactionsOfUserDateBased(user,startDate.atTime(0,0,0), endDate.atTime(23,59,59));
        List<IntraTransaction> intraWalletTransactions = intraTransactionRepository.findByWalletsAndDate(userWallets, startDate.atTime(0,0,0), endDate.atTime(23,59,59));
        for(InterTransaction interWalletTransaction : interWalletTransactions){
            intraWalletTransactions.remove(interWalletTransaction.getDeposit());
            intraWalletTransactions.remove(interWalletTransaction.getWithdrawal());
        }
        List<InterTransactionResponseModel> interTransactionResponseModels = interWalletTransactions.stream().map((transaction -> new InterTransactionResponseModel(transaction.getInterWalletTransactionId(), transaction.getSender().getUsername(), transaction.getSenderWalletId(), transaction.getReceiver().getUsername(), transaction.getReceiverWalletId(), transaction.getDeposit(), transaction.getWithdrawal()))).collect(Collectors.toList());

        return new TransactionsResponseModel(interTransactionResponseModels, intraWalletTransactions);
    }

}