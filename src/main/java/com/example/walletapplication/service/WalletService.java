package com.example.walletapplication.service;

import com.example.walletapplication.entity.IntraTransaction;
import com.example.walletapplication.entity.User;
import com.example.walletapplication.entity.Wallet;
import com.example.walletapplication.enums.IntraTransactionType;
import com.example.walletapplication.exception.AuthenticationFailedException;
import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.InvalidAmountException;
import com.example.walletapplication.exception.WalletNotFoundException;
import com.example.walletapplication.repository.IntraTransactionRepository;
import com.example.walletapplication.repository.UserRepository;
import com.example.walletapplication.repository.WalletRepository;
import com.example.walletapplication.requestModels.WalletRequestModel;
import com.example.walletapplication.responseModels.WalletResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class WalletService {

    @Autowired
    private WalletRepository walletReposiotry;

    @Autowired
    private UserRepository userReposiotry;

    @Autowired
    private IntraTransactionRepository intraTransactionRepository;

    public WalletService(WalletRepository walletReposiotry, UserRepository userReposiotry, IntraTransactionRepository intraTransactionRepository) {
        this.walletReposiotry = walletReposiotry;
        this.userReposiotry = userReposiotry;
        this.intraTransactionRepository = intraTransactionRepository;

    }

    public List<WalletResponseModel> getAllWallets() {
        List<Wallet> wallets = walletReposiotry.findAll();
        List<WalletResponseModel> response = new ArrayList<>();
        for(Wallet wallet : wallets){
            response.add(new WalletResponseModel(wallet.getWalletId(), wallet.getMoney()));
        }
        return response;
    }

    public WalletResponseModel deposit(int walletId, String username, WalletRequestModel requestModel) throws InvalidAmountException, AuthenticationFailedException, WalletNotFoundException {
        User user = userReposiotry.findByUsername(username).orElseThrow(() -> new AuthenticationFailedException("Username or password does not match."));
        Wallet wallet = walletReposiotry.findById(walletId).orElseThrow(() -> new WalletNotFoundException("Wallet id does not match."));
        if(!user.getWallets().contains(wallet))
            throw new AuthenticationFailedException("Wallet id does not match.");

        wallet.deposit(requestModel.getMoney());

        walletReposiotry.save(wallet);
        intraTransactionRepository.save(new IntraTransaction(requestModel.getMoney(), IntraTransactionType.DEPOSIT, wallet, LocalDateTime.now()));
        return new WalletResponseModel(walletId, wallet.getMoney());
    }

    public WalletResponseModel withdraw(int walletId, String username, WalletRequestModel requestModel) throws InsufficientBalanceException, InvalidAmountException, AuthenticationFailedException, WalletNotFoundException {
        User user = userReposiotry.findByUsername(username).orElseThrow(() -> new AuthenticationFailedException("Username or password does not match."));
        Wallet wallet = walletReposiotry.findById(walletId).orElseThrow(() -> new WalletNotFoundException("Wallet id does not match."));
        if(!user.getWallets().contains(wallet))
            throw new AuthenticationFailedException("Wallet id does not match.");

        wallet.withdraw(requestModel.getMoney());

        walletReposiotry.save(wallet);
        intraTransactionRepository.save(new IntraTransaction(requestModel.getMoney(), IntraTransactionType.WITHDRAWAL, wallet, LocalDateTime.now()));
        return new WalletResponseModel(walletId, wallet.getMoney());
    }

}