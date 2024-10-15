package com.example.walletapplication.responseModels;
import com.example.walletapplication.entity.IntraTransaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterTransactionResponseModel {

    private int interWalletTransactionId;
    private String sender;
    private int senderWalletId;
    private String receiver;
    private int receiverWalletId;
    private IntraTransaction deposit;
    private IntraTransaction withdrawal;
}