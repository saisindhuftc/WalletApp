package com.example.walletapplication.requestDTO;

import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.enums.TransactionType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class InterTransactionRequestDTO {
    private Long senderWalletId;
    private Long receiverWalletId;
    private Double amount;
    private TransactionType transactionType;
    private CurrencyType currency;
    private LocalDateTime timestamp;

    public InterTransactionRequestDTO(Long senderWalletId, Long receiverWalletId, Double amount, CurrencyType currency) {
        this.senderWalletId = senderWalletId;
        this.receiverWalletId = receiverWalletId;
        this.amount = amount;
        this.currency = currency;
    }

    public InterTransactionRequestDTO(){

    }
}