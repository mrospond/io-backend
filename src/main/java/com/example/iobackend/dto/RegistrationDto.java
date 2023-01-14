package com.example.iobackend.dto;

import com.example.iobackend.validation.PasswordMatches;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@PasswordMatches
public class RegistrationDto {
    private static final String PASSWORD_MESSAGE = "Password needs to contain minimum 8 characters," +
            " at least one uppercase letter, one lowercase letter, one number and one special character";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$";

    @NotBlank(message = "Username is mandatory")
    @Size(min = 4, max = 20, message = "Username must be from 4 to 20 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username must be alphanumeric")
    private String username;
    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_MESSAGE)
    private String password;
    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_MESSAGE)
    private String passwordConfirmation;
}
