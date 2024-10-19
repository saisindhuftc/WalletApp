package com.example.walletapplication.controller;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.requestDTO.UserRequestDTO;
import com.example.walletapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody UserRequestDTO userRequestModel) {
        String username = userRequestModel.getUsername();
        String password = userRequestModel.getPassword();
        User user = userService.registerUser(username, password, null);
        return ResponseEntity.ok(Map.of(
                "message", "User registration successful",
                "user", user
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(Map.of(
                "message", "User details fetched successfully",
                "user", user
        ));
    }
}
