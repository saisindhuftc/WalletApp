package com.example.walletapplication.controller;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @DeleteMapping("/{user_id}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable("user_id") int userId) throws UserNotFoundException {
        String response = userService.delete(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{user_id}/wallet/{wallet_id}/add")
    public ResponseEntity<User> addWallet(@PathVariable("user_id") int userId) throws UserNotFoundException {
        User returnedUser = userService.addWallet(userId);
        return new ResponseEntity<>(returnedUser, HttpStatus.CREATED);
    }
}
