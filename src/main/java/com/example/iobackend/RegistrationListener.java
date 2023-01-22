package com.example.iobackend;

import com.example.iobackend.database.entities.UserModel;
import com.example.iobackend.service.UserLoginRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private final UserLoginRegisterService service;
    private final JavaMailSender mailSender;

    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        UserModel user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
        String message = "Hello " + user.getUsername() + "! Please activate your account with a link below: ";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);

        email.setText(String.format("%s\r%nhttp://%s:%s%s", message, serverAddress, serverPort, confirmationUrl));
        mailSender.send(email);
    }
}
