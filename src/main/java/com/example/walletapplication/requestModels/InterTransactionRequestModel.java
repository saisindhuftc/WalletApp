package com.example.walletapplication.requestModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterTransactionRequestModel {

    private int senderWalletId;
    private String receiverName;
    private int receiverWalletId;
}