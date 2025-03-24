package com.example.labbackend.controller;

import com.example.labbackend.model.User;
import com.example.labbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request) {
        Optional<User> found = userService.getUser(request.getUsername());
        if (found.isPresent() && found.get().getPassword().equals(request.getPassword())) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.getUser(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken");
        }
        userService.registerUser(user);
        return ResponseEntity.ok("User registered");
    }
    
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestParam String username) {
        Optional<User> user = userService.getUser(username);
        return user.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/api/profile")
    public ResponseEntity<?> updateProfile(@RequestBody User updated) {
        try {
            userService.updateUser(updated);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }


}
