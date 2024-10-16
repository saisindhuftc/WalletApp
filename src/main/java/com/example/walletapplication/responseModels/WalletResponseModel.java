package com.example.walletapplication.responseModels;

import com.example.walletapplication.entity.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WalletResponseModel {

    private Integer walletId;
    private Money money;
}