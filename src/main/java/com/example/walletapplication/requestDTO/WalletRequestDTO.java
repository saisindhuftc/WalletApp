package com.example.walletapplication.requestDTO;

import com.example.walletapplication.enums.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletRequestDTO {

    private Double amount;
    private CurrencyType currency;

}
