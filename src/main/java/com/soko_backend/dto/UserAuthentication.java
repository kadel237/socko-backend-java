package com.soko_backend.dto;

public record UserAuthentication(
        String login,
        String token
){
}
