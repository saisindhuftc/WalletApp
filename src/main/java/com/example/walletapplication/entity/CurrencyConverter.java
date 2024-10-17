package com.example.walletapplication.entity;
import com.example.walletapplication.enums.Currency;

public class CurrencyConverter {

    // Convert an amount from sourceCurrency to targetCurrency
    public static double convertMoney(double amount, Currency sourceCurrency, Currency targetCurrency) {
        if (sourceCurrency == targetCurrency) {
            // No conversion needed if both currencies are the same
            return amount;
        }
        // Conversion factor from source currency to base currency (e.g., INR)
        double sourceToBaseRate = sourceCurrency.getConversionFactor();
        // Conversion factor from target currency to base currency
        double baseToTargetRate = targetCurrency.getConversionFactor();

        // Calculate the equivalent amount in the base currency
        double amountInBaseCurrency = amount * sourceToBaseRate;  // Convert to base currency (e.g., INR)
        // Convert the base currency amount to target currency
        double amountInTargetCurrency = amountInBaseCurrency / baseToTargetRate;

        return amountInTargetCurrency;
    }
}
