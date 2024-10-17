package com.example.walletapplication.entity;

import com.example.walletapplication.exception.InvalidUsernameAndPasswordException;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
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

    public User(String username, String password) {
        if(username == null || password == null || username.isBlank() || password.isBlank()) {
            throw new InvalidUsernameAndPasswordException("Username and password cannot be null or blank");
        }
        this.username = username;
        this.password = password;
        this.wallet = new Wallet();
    }
}