package com.microservice.userDetails.controller;


import com.microservice.userDetails.dto.AuthResponse;
import com.microservice.userDetails.dto.UserLoginDto;
import com.microservice.userDetails.dto.UserRegistrationDto;
import com.microservice.userDetails.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@SuppressWarnings("unused")
@RequiredArgsConstructor
// The @SuppressWarnings("unused") annotation is used to avoid warnings about unused code.
// This is useful during development when the class is not yet fully implemented but is expected to be
// used in the future.
public class AuthController {

    private final AuthService authService;


    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        // This method will handle user registration requests.
        // You can implement the logic to register a new user here.
        AuthResponse authResponse = authService.register(userRegistrationDto);
        return ResponseEntity.ok(authResponse);
    }
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        // This method will handle user login requests.
        // You can implement the logic to authenticate a user here.
        AuthResponse authResponse = authService.login(userLoginDto);
        return ResponseEntity.ok(authResponse);
    }
}
