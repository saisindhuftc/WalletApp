package com.example.walletapplication.service;

import com.example.walletapplication.enums.CurrencyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import proto.ConvertRequest;
import proto.ConvertResponse;
import proto.CurrencyConverterGrpc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CurrencyConverterTest {

    @Mock
    private CurrencyConverterGrpc.CurrencyConverterBlockingStub blockingStub;

    private CurrencyConverter currencyConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyConverter = new CurrencyConverter(blockingStub);
    }

    @Test
    void testConvertCurrencyToINR() {
        ConvertResponse mockResponse = ConvertResponse.newBuilder()
                .setConvertedAmount(7400)
                .build();
        when(blockingStub.convert(any(ConvertRequest.class))).thenReturn(mockResponse);

        double result = currencyConverter.convertMoney(100, CurrencyType.USD, CurrencyType.INR);
        assertEquals(7400, result);
    }

    @Test
    void testConvertCurrencyToUSDFromINR() {
        ConvertResponse mockResponse = ConvertResponse.newBuilder()
                .setConvertedAmount(100)
                .build();
        when(blockingStub.convert(any(ConvertRequest.class))).thenReturn(mockResponse);

        double result = currencyConverter.convertMoney(7400, CurrencyType.INR, CurrencyType.USD);
        assertEquals(100, result);
    }

    @Test
    void testConvertCurrencyToEURFromUSD() {
        ConvertResponse mockResponse = ConvertResponse.newBuilder()
                .setConvertedAmount(85)
                .build();
        when(blockingStub.convert(any(ConvertRequest.class))).thenReturn(mockResponse);

        double result = currencyConverter.convertMoney(100, CurrencyType.USD, CurrencyType.EUR);
        assertEquals(85, result);
    }

    @Test
    void testConvertCurrencyToUSDFromEUR() {
        ConvertResponse mockResponse = ConvertResponse.newBuilder()
                .setConvertedAmount(100)
                .build();
        when(blockingStub.convert(any(ConvertRequest.class))).thenReturn(mockResponse);

        double result = currencyConverter.convertMoney(85, CurrencyType.EUR, CurrencyType.USD);
        assertEquals(100, result);
    }

    @Test
    void testConvertCurrencySameCurrency() {
        ConvertResponse mockResponse = ConvertResponse.newBuilder()
                .setConvertedAmount(100)
                .build();
        when(blockingStub.convert(any(ConvertRequest.class))).thenReturn(mockResponse);

        double result = currencyConverter.convertMoney(100, CurrencyType.USD, CurrencyType.USD);
        assertEquals(100, result);
    }

    @Test
    void testConvertCurrencyZeroAmount() {
        ConvertResponse mockResponse = ConvertResponse.newBuilder()
                .setConvertedAmount(0)
                .build();
        when(blockingStub.convert(any(ConvertRequest.class))).thenReturn(mockResponse);

        double result = currencyConverter.convertMoney(0, CurrencyType.USD, CurrencyType.INR);
        assertEquals(0, result);
    }

    @Test
    void testConvertCurrencyNegativeAmount() {
        ConvertResponse mockResponse = ConvertResponse.newBuilder()
                .setConvertedAmount(-7400)
                .build();
        when(blockingStub.convert(any(ConvertRequest.class))).thenReturn(mockResponse);

        double result = currencyConverter.convertMoney(-100, CurrencyType.USD, CurrencyType.INR);
        assertEquals(-7400, result);
    }

    @Test
    void testConvertMoneySuccess() {
        ConvertResponse mockResponse = ConvertResponse.newBuilder()
                .setConvertedAmount(110)
                .build();
        when(blockingStub.convert(any(ConvertRequest.class))).thenReturn(mockResponse);

        double result = currencyConverter.convertMoney(100, CurrencyType.USD, CurrencyType.EUR);
        assertEquals(110, result);
    }

    @Test
    void testConvertMoneyNullSourceCurrency() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            currencyConverter.convertMoney(100, null, CurrencyType.INR);
        });
        assertEquals("Currency type cannot be null", exception.getMessage());
    }

    @Test
    void testConvertMoneyNullTargetCurrency() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            currencyConverter.convertMoney(100, CurrencyType.USD, null);
        });
        assertEquals("Currency type cannot be null", exception.getMessage());
    }
}
