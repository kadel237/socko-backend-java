package com.soko_backend.exception;

public class UserNotfoundException extends RuntimeException {
    public UserNotfoundException(String s) {
        super("cet utilisateur avec le nom est introuvable");
    }
}
