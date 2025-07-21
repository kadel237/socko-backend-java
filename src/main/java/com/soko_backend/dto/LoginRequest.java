package com.soko_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


public record LoginRequest (
    @NotBlank(message = "Login is mandatory")String email,
    @NotBlank(message = "password is mandatory") String password){
}
