package com.hadef.movieslist.controller;

import com.hadef.movieslist.domain.RefreshTokenRequest;
import com.hadef.movieslist.domain.TokenPair;
import com.hadef.movieslist.domain.dto.LoginRequest;
import com.hadef.movieslist.domain.dto.RegisterRequest;
import com.hadef.movieslist.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest request) {
        log.info("Received request to register user");
        authService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenPair> login(@Valid @RequestBody LoginRequest request) {
        TokenPair login = authService.login(request);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenPair> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        TokenPair tokenPair = authService.refreshToken(request);
        return  ResponseEntity.ok(tokenPair);
    }
}
