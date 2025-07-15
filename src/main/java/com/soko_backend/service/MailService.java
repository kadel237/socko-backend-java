package com.soko_backend.service;

public interface MailService {
    void sendResetCode(String to, String code);
}
