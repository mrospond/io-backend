package com.example.iobackend.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
public class LoginDto {
    @NotBlank(message = "Username is mandatory")
    @Size(min = 4, max = 20, message = "Username must be from 4 to 20 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username must be alphanumeric")
    private String username;
    @NotBlank(message = "Password is mandatory")
    private String password;
}
