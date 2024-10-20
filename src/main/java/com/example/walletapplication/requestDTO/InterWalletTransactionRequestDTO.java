package com.example.walletapplication.requestDTO;

import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.enums.TransactionType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class InterWalletTransactionRequestDTO {
    private Long senderWalletId;
    private Long receiverWalletId;
    private Double amount;
    private TransactionType transactionType;
    private CurrencyType currency;
    private LocalDateTime timestamp;

    public InterWalletTransactionRequestDTO(Long senderWalletId, Long receiverWalletId, Double amount, CurrencyType currency) {
        this.senderWalletId = senderWalletId;
        this.receiverWalletId = receiverWalletId;
        this.amount = amount;
        this.currency = currency;
    }

    public InterWalletTransactionRequestDTO(){

    }
}