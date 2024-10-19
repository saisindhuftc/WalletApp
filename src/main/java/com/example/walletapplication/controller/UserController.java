package com.example.walletapplication.controller;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.exception.InvalidUsernameAndPasswordException;
import com.example.walletapplication.exception.UnAuthorisedUserException;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.requestDTO.UserRequestDTO;
import com.example.walletapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO userRequestModel) {
        String username = userRequestModel.getUsername();
        String password = userRequestModel.getPassword();
        try {
            User user = userService.registerUser(username, password, null);
            return ResponseEntity.ok(user);
        } catch (InvalidUsernameAndPasswordException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserDetails(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (UnAuthorisedUserException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

}
