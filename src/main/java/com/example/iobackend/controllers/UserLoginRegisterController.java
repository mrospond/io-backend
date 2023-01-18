package com.example.iobackend.controllers;

import com.example.iobackend.OnRegistrationCompleteEvent;
import com.example.iobackend.database.entities.UserModel;
import com.example.iobackend.database.entities.VerificationToken;
import com.example.iobackend.dto.LoginDto;
import com.example.iobackend.dto.RegistrationDto;
import com.example.iobackend.exceptions.TokenExpiredException;
import com.example.iobackend.service.UserLoginRegisterService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;

@AllArgsConstructor
@RestController
public class UserLoginRegisterController {
    private final UserLoginRegisterService userLoginRegisterService;
    private final ApplicationEventPublisher eventPublisher;

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody @Valid RegistrationDto registrationDto,
                                               HttpServletRequest request) {
        UserModel user = userLoginRegisterService.registerNewUser(registrationDto);
        String appUrl = request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), appUrl));
        return ResponseEntity.ok("Please activate your account using the activation link that was sent to your email");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@RequestBody @Valid LoginDto loginDto) {
        userLoginRegisterService.login(loginDto);
        return ResponseEntity.ok("User: " + loginDto.getUsername() + " logged in successfully!");
    }

    @GetMapping("/registrationConfirm")
    public ResponseEntity<Object> confirmRegistration(@RequestParam String token) {
        VerificationToken verificationToken = userLoginRegisterService.getVerificationToken(token);

        UserModel user = verificationToken.getUser();
        if (!verificationToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            throw new TokenExpiredException("This token has already expired");
        }

        user.setEnabled(true);
        userLoginRegisterService.updateUser(user);
        return ResponseEntity.ok().body("Account activated successfully");
    }
}
