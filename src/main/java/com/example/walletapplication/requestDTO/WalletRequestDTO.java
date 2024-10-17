package com.example.walletapplication.requestDTO;

import com.example.walletapplication.enums.Currency;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletRequestDTO {

    private Double amount;
    private Currency currency;

}
