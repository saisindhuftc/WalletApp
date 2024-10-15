package com.example.walletapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Currency;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDTO {
    private Currency currency;
    private double value;
}