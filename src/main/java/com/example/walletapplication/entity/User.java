package com.example.walletapplication.entity;

import com.example.walletapplication.exception.InvalidUsernameAndPasswordException;
import jakarta.persistence.*;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Wallet wallet;

    public User() {
    }

    public User(String username, String password) {
        if (!isValidUser(username, password)) {
            throw new InvalidUsernameAndPasswordException("Invalid user username and password must not be null or empty");
        }
        this.username = username;
        this.password = password;
        this.wallet = new Wallet(this);
    }

    public Double deposit(Double amount) {
        return wallet.deposit(amount);
    }

    public Double withdraw(Double amount) {
        return wallet.withdraw(amount);
    }

    private boolean isValidUser(String username, String password) {
        return username != null && !username.isEmpty() && password != null && !password.isEmpty();
    }
}