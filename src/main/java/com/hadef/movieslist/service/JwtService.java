package com.hadef.movieslist.service;

import com.hadef.movieslist.domain.TokenPair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateAccessToken(Authentication authentication);
    String generateRefreshToken(Authentication authentication);
    boolean validateTokenForUser(String token, UserDetails userDetails);
    boolean isRefreshToken(String token);
    boolean isValidToken(String token);
    String extractEmailFromToken(String token);
    TokenPair generateTokenPair(Authentication authentication);
}
