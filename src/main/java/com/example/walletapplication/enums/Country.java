package com.example.walletapplication.enums;


import static com.example.walletapplication.enums.Currency.*;

public enum Country {

    INDIA(INR),
    USA(USD),
    EUROPE(EUR);

    private final Currency currency;

    Country(Currency currency) {
        this.currency = currency;
    }

    public Currency getCurrency() {
        return this.currency;
    }
}
