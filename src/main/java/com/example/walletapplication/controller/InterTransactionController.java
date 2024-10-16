package com.example.walletapplication.controller;

import com.example.walletapplication.exception.SameWalletsForTransactionException;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.exception.WalletNotFoundException;
import com.example.walletapplication.requestModels.InterTransactionRequestModel;
import com.example.walletapplication.responseModels.InterTransactionResponseModel;
import com.example.walletapplication.service.InterTransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/inter-transactions")
public class InterTransactionController {

    @Autowired
    private InterTransactionService interTransactionService;

    @PostMapping("")
    public ResponseEntity<?> transact(@RequestBody InterTransactionRequestModel interWalletTransactionRequestModel) {
        try {
            InterTransactionResponseModel response = interTransactionService.transact(interWalletTransactionRequestModel);
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (WalletNotFoundException e) {
            return new ResponseEntity<>("Wallet not found", HttpStatus.NOT_FOUND);
        } catch (SameWalletsForTransactionException e) {
            return new ResponseEntity<>("Cannot transact between the same wallets", HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
}