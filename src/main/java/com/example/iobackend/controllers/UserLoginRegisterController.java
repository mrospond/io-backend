package com.example.iobackend.controllers;

import com.example.iobackend.dto.LoginDto;
import com.example.iobackend.dto.RegistrationDto;
import com.example.iobackend.service.UserLoginRegisterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserLoginRegisterController {
    private final UserLoginRegisterService userLoginRegisterService;

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody @Valid RegistrationDto registrationDto) {
        userLoginRegisterService.registerNewUser(registrationDto);
        return ResponseEntity.ok("User: " + registrationDto.getUsername() + " registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@RequestBody @Valid LoginDto loginDto) {
        userLoginRegisterService.login(loginDto);
        return ResponseEntity.ok("User: " + loginDto.getUsername() + " logged in successfully!");
    }
}
