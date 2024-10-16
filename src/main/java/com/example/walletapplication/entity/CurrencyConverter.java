package com.example.walletapplication.entity;

import com.example.walletapplication.enums.Currency;

public class CurrencyConverter {

    // Convert from sourceCurrency to targetCurrency
    public static Money convertMoney(Money money, Currency sourceCurrency, Currency targetCurrency) {
        // Conversion factor from source currency to target currency
        double sourceToBaseRate = sourceCurrency.getConversionFactor();  // Convert to base (INR)
        double targetToBaseRate = targetCurrency.getConversionFactor();  // Convert to target

        // Calculate the equivalent amount in the target currency
        double amountInBaseCurrency = money.getAmount() / sourceToBaseRate;  // Convert to base currency (INR)
        double amountInTargetCurrency = amountInBaseCurrency * targetToBaseRate;  // Convert to target currency

        // Return a new Money object with the converted amount and target currency
        return new Money(amountInTargetCurrency, targetCurrency);
    }
}
