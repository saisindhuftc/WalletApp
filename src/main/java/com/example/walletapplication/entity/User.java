package com.example.walletapplication.entity;

import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.exception.InvalidUsernameAndPasswordException;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    public User() {
        this.currencyType = CurrencyType.INR;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    private String password;
    private CurrencyType currencyType;

    @OneToOne(cascade = CascadeType.ALL)
    private Wallet wallet;

    public User(String username, String password, CurrencyType currencyType) {
        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            throw new InvalidUsernameAndPasswordException("Username and password cannot be null or blank");
        }
        this.username = username;
        this.password = password;
        // Set the default currency type if not provided
        if(currencyType == null) {
            this.currencyType = CurrencyType.INR;
        } else {
            this.currencyType = currencyType;
        }
        this.wallet = new Wallet(this.currencyType);
    }
}
