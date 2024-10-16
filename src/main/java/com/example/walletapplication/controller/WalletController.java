package com.example.walletapplication.controller;

import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.WalletNotFoundException;
import com.example.walletapplication.requestModels.WalletRequestModel;
import com.example.walletapplication.responseModels.WalletResponseModel;
import com.example.walletapplication.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping(value = "/{wallet_id}/intra-wallet-transaction", headers = "type=deposit")
    public ResponseEntity<WalletResponseModel> deposit(@PathVariable("wallet_id") int walletId, @RequestBody WalletRequestModel requestModel) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        WalletResponseModel returnedWallet = walletService.deposit(walletId, username, requestModel);
        if (returnedWallet == null) {
            throw new WalletNotFoundException("Wallet not found with ID: " + walletId);
        }

        return new ResponseEntity<>(returnedWallet, HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/{wallet_id}/intra-wallet-transaction", headers = "type=withdraw")
    public ResponseEntity<WalletResponseModel> withdraw(@PathVariable("wallet_id") int walletId, @RequestBody WalletRequestModel requestModel) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            WalletResponseModel returnedWallet = walletService.withdraw(walletId, username, requestModel);
            return new ResponseEntity<>(returnedWallet, HttpStatus.ACCEPTED);
        } catch (InsufficientBalanceException e) {
            throw new InsufficientBalanceException("Insufficient balance in wallet ID: " + walletId);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<WalletResponseModel>> wallets() {
        List<WalletResponseModel> responseWallets = walletService.getAllWallets();
        return new ResponseEntity<>(responseWallets, HttpStatus.OK);
    }
}
