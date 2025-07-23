package com.soko_backend.dto.auth;

import jakarta.validation.constraints.NotBlank;


public record LoginRequest (
    @NotBlank(message = "Login is mandatory")String email,
    @NotBlank(message = "password is mandatory") String password){
}
