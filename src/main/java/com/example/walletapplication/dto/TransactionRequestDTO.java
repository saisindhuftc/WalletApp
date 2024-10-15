package com.example.walletapplication.dto;

import com.example.walletapplication.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDTO {
    private Long walletId;
    private Double amount;
    private TransactionType transactionType;
    private LocalDateTime timestamp;
}