package com.soko_backend.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String username) {
        super("les identifiants de cet utilisateurs" + username + " ne sont pas correct");
    }
}
