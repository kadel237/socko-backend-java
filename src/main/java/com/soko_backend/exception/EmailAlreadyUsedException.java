package com.soko_backend.exception;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String email) {
        super("cet Email (" +email+ " ) est déja utilisé");
    }
}
