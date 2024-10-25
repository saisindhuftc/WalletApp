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

    @Test
    void testConvertCurrencyLargeAmount() {
        ConvertResponse mockResponse = ConvertResponse.newBuilder()
                .setConvertedAmount(74000000000000.0)
                .build();
        when(blockingStub.convert(any(ConvertRequest.class))).thenReturn(mockResponse);

        double result = currencyConverter.convertMoney(1e12, CurrencyType.USD, CurrencyType.INR);
        assertEquals(74000000000000.0, result);
    }


    @Test
    void testConvertCurrencyInvalidAmount() {
        ConvertResponse mockResponse = ConvertResponse.newBuilder()
                .setConvertedAmount(0)
                .build();
        when(blockingStub.convert(any(ConvertRequest.class))).thenReturn(mockResponse);

        double result = currencyConverter.convertMoney(-1, CurrencyType.USD, CurrencyType.INR);
        assertEquals(0, result);
    }

    @Test
    void testConvertCurrencyNetworkError() {
        when(blockingStub.convert(any(ConvertRequest.class))).thenThrow(new RuntimeException("Network error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            currencyConverter.convertMoney(100, CurrencyType.USD, CurrencyType.INR);
        });
        assertEquals("Network error", exception.getMessage());
    }

    @Test
    void testConvertCurrencyInvalidResponse() {
        ConvertResponse mockResponse = ConvertResponse.newBuilder().build(); // No converted amount set
        when(blockingStub.convert(any(ConvertRequest.class))).thenReturn(mockResponse);

        double result = currencyConverter.convertMoney(100, CurrencyType.USD, CurrencyType.INR);
        assertEquals(0, result); // Expecting 0 as the default for invalid response
    }

    @Test
    void testConvertCurrencyWithPrecision() {
        ConvertResponse mockResponse = ConvertResponse.newBuilder()
                .setConvertedAmount(7400.12345)
                .build();
        when(blockingStub.convert(any(ConvertRequest.class))).thenReturn(mockResponse);

        double result = currencyConverter.convertMoney(100, CurrencyType.USD, CurrencyType.INR);
        assertEquals(7400.12345, result, 0.00001); // Allowing for precision
    }

    @Test
    void testConvertCurrencyWithDifferentSourceTargetOrder() {
        ConvertResponse mockResponse = ConvertResponse.newBuilder()
                .setConvertedAmount(75)
                .build();
        when(blockingStub.convert(any(ConvertRequest.class))).thenReturn(mockResponse);

        double result = currencyConverter.convertMoney(100, CurrencyType.EUR, CurrencyType.USD);
        assertEquals(75, result);
    }
    @Test
    void testConvertCurrencySimultaneousRequests() throws InterruptedException {
        ConvertResponse mockResponse1 = ConvertResponse.newBuilder()
                .setConvertedAmount(7400)
                .build();
        ConvertResponse mockResponse2 = ConvertResponse.newBuilder()
                .setConvertedAmount(18302)
                .build();

        when(blockingStub.convert(any(ConvertRequest.class))).thenReturn(mockResponse1).thenReturn(mockResponse2);

        Thread thread1 = new Thread(() -> {
            double result = currencyConverter.convertMoney(100, CurrencyType.USD, CurrencyType.INR);
            assertEquals(7400, result);
        });

        Thread thread2 = new Thread(() -> {
            double result = currencyConverter.convertMoney(200, CurrencyType.EUR, CurrencyType.INR);
            assertEquals(18302, result);
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }
}