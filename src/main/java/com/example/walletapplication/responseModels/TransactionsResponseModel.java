package com.example.walletapplication.responseModels;

import com.example.walletapplication.entity.IntraTransaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsResponseModel {

    private List<InterTransactionResponseModel> interWalletTransactions;
    private List<IntraTransaction> intraWalletTransactions;
}
