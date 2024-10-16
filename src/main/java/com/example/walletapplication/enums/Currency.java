package com.example.walletapplication.enums;

public enum Currency {

    INR(1.0),
    USD(84.08),
    EUR(91.51);

    private final double conversionFactor;

    Currency(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }}