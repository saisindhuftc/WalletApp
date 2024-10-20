package com.example.walletapplication.requestDTO;

import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntraWalletTransactionRequestDTO {
    private TransactionType transactionType;
    private Double amount;
    private CurrencyType currency;
}
