package com.example.walletapplication.controller;

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

    @PostMapping( "/{wallet_id}/intra-wallet-transaction/deposit")
    public ResponseEntity<WalletResponseModel> deposit(@PathVariable("wallet_id") int walletId, @RequestBody WalletRequestModel requestModel) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        WalletResponseModel returnedWallet = walletService.deposit(walletId, username, requestModel);

        return new ResponseEntity<>(returnedWallet, HttpStatus.ACCEPTED);
    }

    @PostMapping( "/{wallet_id}/intra-wallet-transaction/withdrawal")
    public ResponseEntity<WalletResponseModel> withdraw(@PathVariable("wallet_id") int walletId, @RequestBody WalletRequestModel requestModel) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        WalletResponseModel returnedWallet = walletService.withdraw(walletId, username, requestModel);

        return new ResponseEntity<>(returnedWallet, HttpStatus.ACCEPTED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<WalletResponseModel>> wallets() {
        List<WalletResponseModel> responseWallets = walletService.getAllWallets();
        return new ResponseEntity<>(responseWallets, HttpStatus.OK);
    }
}
