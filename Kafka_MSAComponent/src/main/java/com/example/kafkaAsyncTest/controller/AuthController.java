package com.example.kafkaAsyncTest.controller;

import com.example.kafkaAsyncTest.common.customKafkaFilterSet.JwtUtils;
import com.example.kafkaAsyncTest.entity.User;
import com.example.kafkaAsyncTest.service.defaultService.CustomUserDetailsService;
import com.example.kafkaAsyncTest.service.defaultService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            User user = userService.getUserByUsername(username);

            String token = jwtUtils.generateToken(user);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", String.valueOf(user.getUserId()));

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.getUserByUsername(user.getUsername()) != null) {
            return ResponseEntity.status(409).body("Username already exists");
        }

        User newUser = userService.createUser(user);
        return ResponseEntity.ok(newUser);
    }

}