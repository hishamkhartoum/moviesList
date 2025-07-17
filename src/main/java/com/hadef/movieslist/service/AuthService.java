package com.hadef.movieslist.service;

import com.hadef.movieslist.domain.RefreshTokenRequest;
import com.hadef.movieslist.domain.TokenPair;
import com.hadef.movieslist.domain.dto.LoginRequest;
import com.hadef.movieslist.domain.dto.RegisterRequest;
import jakarta.validation.Valid;

public interface AuthService {
    void registerUser(RegisterRequest request);
    TokenPair login(LoginRequest loginRequest);
    TokenPair refreshToken(@Valid RefreshTokenRequest request);
}
