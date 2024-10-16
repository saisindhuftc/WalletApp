package com.example.walletapplication.entity;

import com.example.walletapplication.enums.Country;
import com.example.walletapplication.enums.Currency;
import com.example.walletapplication.enums.IntraTransactionType;
import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.InvalidAmountException;
import com.example.walletapplication.requestModels.InterTransactionRequestModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer walletId;

    private Money money;

    public Wallet(Country country) {
        this.money = new Money(0.0, country.getCurrency());
    }

    public void deposit(Money money) throws InvalidAmountException {
        this.money.add(money);
    }

    public void withdraw(Money money) throws InsufficientBalanceException, InvalidAmountException {
        this.money.subtract(money);
    }

    public InterTransaction transact(InterTransactionRequestModel requestModel, User sender, Wallet receiverWallet, User receiver) throws InsufficientBalanceException, InvalidAmountException {
        Money convertedMoney = CurrencyConverter.convertMoney(requestModel.getMoney(), this.getMoney().getCurrency(), receiverWallet.getMoney().getCurrency());

        this.withdraw(requestModel.getMoney());
        IntraTransaction withdrawTransaction = new IntraTransaction(
                new Money(requestModel.getMoney().getAmount(), requestModel.getMoney().getCurrency()),
                IntraTransactionType.WITHDRAWAL, this, LocalDateTime.now()
        );

        receiverWallet.deposit(convertedMoney);
        IntraTransaction depositTransaction = new IntraTransaction(
                convertedMoney, IntraTransactionType.DEPOSIT, receiverWallet, LocalDateTime.now()
        );

        return new InterTransaction(
                sender, this.getWalletId(), receiver, receiverWallet.getWalletId(),
                depositTransaction, withdrawTransaction
        );
    }

}