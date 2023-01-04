package com.example.iobackend.service;

import com.example.iobackend.database.entities.UserModel;
import com.example.iobackend.database.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findFirstByUsername(username)
                .map(this::createUserDetails)
                .orElseThrow();
    }

    private UserDetails createUserDetails(UserModel user) {
        return User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash())
                .roles("USER")
                .build();
    }
}
