package com.example.walletapplication.requestModels;


import com.example.walletapplication.enums.Country;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequestModel {

    private int userId;
    private String userName;
    private String password;
    private Country country;

}