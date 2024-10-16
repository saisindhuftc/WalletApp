package com.example.walletapplication.controller;

import com.example.walletapplication.responseModels.TransactionsResponseModel;
import com.example.walletapplication.service.IntraTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/transactions")
public class IntraTransactionController {

    @Autowired
    private IntraTransactionService intratransactionService;

    @GetMapping
    public ResponseEntity<TransactionsResponseModel> allTransactions(@RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate){
        if(startDate != null && endDate != null) {
            return new ResponseEntity<>(intratransactionService.allTransactionsDateBased(startDate, endDate), HttpStatus.OK);
        }
        return new ResponseEntity<>(intratransactionService.allTransactions(), HttpStatus.OK);
    }
}
