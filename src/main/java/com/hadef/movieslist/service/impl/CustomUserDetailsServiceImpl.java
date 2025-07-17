package com.hadef.movieslist.service.impl;

import com.hadef.movieslist.domain.entity.User;
import com.hadef.movieslist.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                ()->new EntityNotFoundException("User not found with username: " + username)
        );
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                getAuthority(user)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthority(User user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole().name());
        return List.of(grantedAuthority);
    }
}
