package com.example.walletapplication.requestModels;

import com.example.walletapplication.entity.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WalletRequestModel {

    private Money money;

    public WalletRequestModel(Money money) {
        this.money = money;
    }
}