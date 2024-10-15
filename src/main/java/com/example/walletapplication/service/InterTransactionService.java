package com.example.walletapplication.service;

import com.example.walletapplication.entity.InterTransaction;
import com.example.walletapplication.entity.User;
import com.example.walletapplication.entity.Wallet;
import com.example.walletapplication.exception.*;
import com.example.walletapplication.repository.InterTransactionRepository;
import com.example.walletapplication.repository.IntraTransactionRepository;
import com.example.walletapplication.repository.UserRepository;
import com.example.walletapplication.repository.WalletRepository;
import com.example.walletapplication.requestModels.InterTransactionRequestModel;
import com.example.walletapplication.responseModels.InterTransactionResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class InterTransactionService {

    @Autowired
    private InterTransactionRepository interTransactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private IntraTransactionRepository intraWalletRepository;

    public InterTransactionResponseModel transact(InterTransactionRequestModel requestModel) throws InsufficientBalanceException, InvalidAmountException, UserNotFoundException, WalletNotFoundException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User sender = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User "+ username + " not found."));
        User receiver = userRepository.findByUsername(requestModel.getReceiverName()).orElseThrow(() -> new UserNotFoundException("User "+ requestModel.getReceiverName() + " not found."));
        Wallet senderWallet = walletRepository.findById((long) requestModel.getSenderWalletId()).orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
        Wallet receiverWallet =  walletRepository.findById((long)requestModel.getReceiverWalletId()).orElseThrow(()-> new WalletNotFoundException("Wallet not found"));

        if(!sender.getWallets().contains(senderWallet) || !receiver.getWallets().contains(receiverWallet))
            throw new WalletNotFoundException("Wallet not found");
        if(senderWallet.equals(receiverWallet))
            throw new SameWalletsForTransactionException("Sender and receiver wallets are the same");

        InterTransaction interWalletTransaction = senderWallet.transact(requestModel, sender, receiverWallet, receiver);

        userRepository.save(sender);
        userRepository.save(receiver);
        InterTransaction savedTransaction = interTransactionRepository.save(interWalletTransaction);

        return new InterTransactionResponseModel(savedTransaction.getInterWalletTransactionId(), username, requestModel.getSenderWalletId(), requestModel.getReceiverName(), requestModel.getReceiverWalletId(), savedTransaction.getDeposit(), savedTransaction.getWithdrawal());
    }
}