package com.hadef.movieslist.service.impl;

import com.hadef.movieslist.domain.RefreshTokenRequest;
import com.hadef.movieslist.domain.TokenPair;
import com.hadef.movieslist.domain.dto.LoginRequest;
import com.hadef.movieslist.domain.dto.RegisterRequest;
import com.hadef.movieslist.domain.entity.User;
import com.hadef.movieslist.repository.UserRepository;
import com.hadef.movieslist.service.AuthService;
import com.hadef.movieslist.service.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    @Transactional
    public void registerUser(RegisterRequest request) {
        if(userRepository.existsByUsername(request.getUsername())){
            throw new IllegalArgumentException("Username is already in use");
        }
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(request.getRole())
                .build();
        userRepository.save(user);
    }

    @Override
    public TokenPair login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtService.generateTokenPair(authentication);
    }

    @Override
    public TokenPair refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        if(!jwtService.isValidToken(refreshToken)){
            throw new IllegalArgumentException("Invalid refresh token");
        }
        String user = jwtService.extractUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user);
        if(userDetails==null){
            throw new IllegalArgumentException("Invalid username or password");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
        String accessToken = jwtService.generateAccessToken(authenticationToken);
        return new TokenPair(accessToken,refreshToken);
    }
}
