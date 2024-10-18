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
    public User(){
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    private Wallet wallet;

    public User(String username, String password, CurrencyType currencyType) {
        if(username == null || password == null || username.isBlank() || password.isBlank()) {
            throw new InvalidUsernameAndPasswordException("Username and password cannot be null or blank");
        }
        this.username = username;
        this.password = password;
        if(currencyType == null) currencyType = CurrencyType.INR;
        this.wallet = new Wallet(currencyType);
    }
}