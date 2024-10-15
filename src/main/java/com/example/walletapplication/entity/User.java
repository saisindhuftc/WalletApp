package com.example.walletapplication.entity;

import com.example.walletapplication.entity.Wallet;
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
    private String userName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "USERID")
    private List<Wallet> wallets = new ArrayList<>();

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.wallets.add(new Wallet());
    }
}