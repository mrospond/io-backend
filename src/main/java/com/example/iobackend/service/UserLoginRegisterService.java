package com.example.iobackend.service;

import com.example.iobackend.database.entities.UserModel;
import com.example.iobackend.database.repository.UserRepository;
import com.example.iobackend.dto.LoginDto;
import com.example.iobackend.dto.RegistrationDto;
import com.example.iobackend.exceptions.UsernameAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserLoginRegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public void registerNewUser(RegistrationDto registrationDto) {
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new UsernameAlreadyExistsException("Username " + registrationDto.getUsername() + " already exists");
        }
        UserModel userModel = UserModel.builder()
                .username(registrationDto.getUsername())
                .passwordHash(passwordEncoder.encode(registrationDto.getPassword()))
                .build();
        userRepository.save(userModel);
    }

    public void login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
