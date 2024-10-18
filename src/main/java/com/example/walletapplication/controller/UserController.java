package com.example.walletapplication.controller;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.exception.InvalidUsernameAndPasswordException;
import com.example.walletapplication.exception.UnAuthorisedUserException;
import com.example.walletapplication.requestDTO.UserRequestDTO;
import com.example.walletapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO userRequestModel) {
        try {
            String username = userRequestModel.getUserName();
            String password = userRequestModel.getPassword();
            System.out.println("username: " + username);
            System.out.println("password: " + password);
            User user = userService.register(username, password);
            return ResponseEntity.ok(user);
        } catch (InvalidUsernameAndPasswordException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            String response = userService.delete(userId);
            return ResponseEntity.ok(response);
        } catch (UnAuthorisedUserException e) {
            return ResponseEntity.status(403).body("User not authorized");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserDetails(@PathVariable String username) {
        try {
            UserDetails userDetails = userService.loadUserByUsername(username);
            return ResponseEntity.ok(userDetails);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

}
