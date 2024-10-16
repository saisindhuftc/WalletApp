package com.example.walletapplication.entity;

import com.example.walletapplication.entity.Wallet;
import com.example.walletapplication.enums.Country;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;

    @Column(unique = true)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    private Country country;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "USERID")
    private List<Wallet> wallets = new ArrayList<>();

    public User(String username, String password, Country country) {
        this.username = username;
        this.password = password;
        this.wallets.add(new Wallet(country));
    }
}