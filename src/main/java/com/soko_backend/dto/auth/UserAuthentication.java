package com.soko_backend.dto.auth;

public record UserAuthentication(
        String login,
        String token
){
}
