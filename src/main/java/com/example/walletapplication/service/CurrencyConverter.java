package com.example.walletapplication.service;

import com.example.walletapplication.enums.CurrencyType;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.ConvertRequest;
import proto.ConvertResponse;
import proto.CurrencyConverterGrpc;

public class CurrencyConverter {

    private final CurrencyConverterGrpc.CurrencyConverterBlockingStub blockingStub;

    public CurrencyConverter() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        blockingStub = CurrencyConverterGrpc.newBlockingStub(channel);
    }

    // Constructor for testing
    public CurrencyConverter(CurrencyConverterGrpc.CurrencyConverterBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    public double convertMoney(double amount, CurrencyType sourceCurrency, CurrencyType targetCurrency) {
        if (sourceCurrency == null || targetCurrency == null) {
            throw new IllegalArgumentException("Currency type cannot be null");
        }

        ConvertRequest request = ConvertRequest.newBuilder()
                .setAmount(amount)
                .setSourceCurrency(sourceCurrency.name())
                .setTargetCurrency(targetCurrency.name())
                .build();

        ConvertResponse response = blockingStub.convert(request);
        return response.getConvertedAmount();
    }
}