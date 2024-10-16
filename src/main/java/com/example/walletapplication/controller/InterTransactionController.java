package com.example.walletapplication.controller;

import com.example.walletapplication.requestModels.InterTransactionRequestModel;
import com.example.walletapplication.responseModels.InterTransactionResponseModel;
import com.example.walletapplication.service.InterTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inter-transactions")
public class InterTransactionController {

    @Autowired
    private InterTransactionService interWalletTransactionService;

    @PostMapping("")
    public ResponseEntity<InterTransactionResponseModel> transact(@RequestBody InterTransactionRequestModel interWalletTransactionRequestModel) {
        InterTransactionResponseModel response = interWalletTransactionService.transact(interWalletTransactionRequestModel);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}

