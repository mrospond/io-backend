package com.example.iobackend.controllers;

import com.example.iobackend.OnRegistrationCompleteEvent;
import com.example.iobackend.database.entities.UserModel;
import com.example.iobackend.dto.LoginDto;
import com.example.iobackend.dto.RegistrationDto;
import com.example.iobackend.service.UserLoginRegisterService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserLoginRegisterController {
    private final UserLoginRegisterService userLoginRegisterService;
    private final ApplicationEventPublisher eventPublisher;

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody @Valid RegistrationDto registrationDto,
                                               HttpServletRequest request) {
        UserModel user = userLoginRegisterService.registerNewUser(registrationDto);
        String appUrl = request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), appUrl));
        return ResponseEntity.ok("User: " + registrationDto.getUsername() + " registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@RequestBody @Valid LoginDto loginDto) {
        userLoginRegisterService.login(loginDto);
        return ResponseEntity.ok("User: " + loginDto.getUsername() + " logged in successfully!");
    }

    @GetMapping("/regitrationConfirm")
    public void confirmRegistration(WebRequest request, @RequestParam("token") String token) {

    }
}
