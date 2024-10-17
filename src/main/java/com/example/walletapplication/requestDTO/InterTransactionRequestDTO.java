package com.example.walletapplication.requestDTO;

import com.example.walletapplication.enums.Currency;
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
    private Currency currency;
    private LocalDateTime timestamp;
}