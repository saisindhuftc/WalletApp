package com.example.walletapplication.controller;

import com.example.walletapplication.exception.*;
import com.example.walletapplication.requestModels.InterTransactionRequestModel;
import com.example.walletapplication.responseModels.InterTransactionResponseModel;
import com.example.walletapplication.service.InterTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

// Class-level path: /api/v1/inter-transactions
@Controller
@RequestMapping("/api/v1/inter-transactions")
public class InterTransactionController {

    @Autowired
    private InterTransactionService interWalletTransactionService;

    // Method-level path: POST /api/v1/inter-transactions
    @PostMapping("")
    public ResponseEntity<InterTransactionResponseModel> transact(@RequestBody InterTransactionRequestModel interWalletTransactionRequestModel) throws InsufficientBalanceException, InvalidAmountException, UserNotFoundException, WalletNotFoundException, SameWalletsForTransactionException {
        InterTransactionResponseModel response = interWalletTransactionService.transact(interWalletTransactionRequestModel);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}