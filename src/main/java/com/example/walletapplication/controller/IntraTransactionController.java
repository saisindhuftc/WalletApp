package com.example.walletapplication.controller;

import com.example.walletapplication.exception.TransactionNotFoundException;
import com.example.walletapplication.responseModels.TransactionsResponseModel;
import com.example.walletapplication.service.IntraTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/api/v1/transactions")
public class IntraTransactionController {

    @Autowired
    private IntraTransactionService intratransactionService;

    @GetMapping("")
    public ResponseEntity<?> allTransactions(@RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate) {
        try {
            if (startDate != null && endDate != null) {
                return new ResponseEntity<>(intratransactionService.allTransactionsDateBased(startDate, endDate), HttpStatus.OK);
            }
            return new ResponseEntity<>(intratransactionService.allTransactions(), HttpStatus.OK);
        } catch (TransactionNotFoundException e) {
            return new ResponseEntity<>("Transactions not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching transactions", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}