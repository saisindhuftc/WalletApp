package com.example.walletapplication.controller;

import com.example.walletapplication.dto.UserRequestDTO;
import com.example.walletapplication.dto.UserResponseDTO;
import com.example.walletapplication.entity.User;
import com.example.walletapplication.exception.InvalidCredentialsException;
import com.example.walletapplication.exception.UserAlreadyExistsException;
import com.example.walletapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            User user = userService.registerUser(userRequestDTO.getUsername(), userRequestDTO.getPassword());
            UserResponseDTO userResponseDTO = new UserResponseDTO(user.getId(), user.getUsername());
            return ResponseEntity.ok(userResponseDTO);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(409).body(e.getMessage()); // Conflict
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            User user = userService.loginUser(userRequestDTO.getUsername(), userRequestDTO.getPassword());
            UserResponseDTO userResponseDTO = new UserResponseDTO(user.getId(), user.getUsername());
            return ResponseEntity.ok(userResponseDTO);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(401).body(e.getMessage()); // Unauthorized
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserByUserId(@PathVariable Long id) {
        User user = userService.getUserByUserId(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserResponseDTO userResponseDTO = new UserResponseDTO(user.getId(), user.getUsername());
        return ResponseEntity.ok(userResponseDTO);
    }
}