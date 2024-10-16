package com.example.walletapplication.controller;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @DeleteMapping("/{user_id}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable("user_id") int userId) throws UserNotFoundException {
        try {
            String response = userService.delete(userId);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while deleting the user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{user_id}/wallet/{wallet_id}/add")
    public ResponseEntity<User> addWallet(@PathVariable("user_id") int userId) throws UserNotFoundException {
        try {
            User returnedUser = userService.addWallet(userId);
            return new ResponseEntity<>(returnedUser, HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
