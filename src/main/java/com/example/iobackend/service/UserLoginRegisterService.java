package com.example.iobackend.service;

import com.example.iobackend.database.entities.UserModel;
import com.example.iobackend.database.repository.UserRepository;
import com.example.iobackend.dto.LoginDto;
import com.example.iobackend.dto.RegistrationDto;
import com.example.iobackend.exceptions.UsernameAlreadyExistsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class UserLoginRegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public void registerNewUser(RegistrationDto registrationDto) {
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            String message = "Username " + registrationDto.getUsername() + " already exists";
            log.error(message);
            throw new UsernameAlreadyExistsException(message);
        }
        UserModel userModel = UserModel.builder()
                .username(registrationDto.getUsername())
                .passwordHash(passwordEncoder.encode(registrationDto.getPassword()))
                .build();
        userRepository.save(userModel);
        log.info("User registered: " + registrationDto.getUsername());
    }

    public void login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("User logged in: " + loginDto.getUsername());
    }
}
