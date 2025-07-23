package com.soko_backend.service.mail;

public interface MailService {
    void sendVerificationEmail(String to, String verificationLink);
    void sendPasswordResetEmail(String to, String resetLink);
}