package com.soko_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class RegisterRequest {
    @NotBlank(message = "Login is mandatory")   private String name;
    @NotBlank(message = "surname is mandatory")private String surname;
    private String username;
    @NotBlank(message = "email is mandatory") private String email;
    @NotBlank(message = "password is mandatory") private String password;
}
