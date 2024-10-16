package com.example.walletapplication.requestModels;

import com.example.walletapplication.entity.Money;
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
    private Money money;
}