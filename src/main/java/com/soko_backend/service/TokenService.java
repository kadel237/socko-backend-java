package com.soko_backend.service;

import com.soko_backend.entity.PasswordResetToken;
import com.soko_backend.entity.UserEntity;
import com.soko_backend.entity.VerificationToken;
import com.soko_backend.exception.TokenNotFoundException;
import com.soko_backend.repository.PasswordResetTokenRepository;
import com.soko_backend.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final int EXPIRATION_MINUTES = 30;

    //  ========== VERIFICATION TOKEN ==========

    public VerificationToken createVerificationToken(UserEntity user) {
        verificationTokenRepository.deleteByUser_Id(user.getId()); // remove old if exists
        VerificationToken token = VerificationToken.create(user, EXPIRATION_MINUTES);
        return verificationTokenRepository.save(token);
    }

    public VerificationToken validateVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token)
                .filter(t -> !t.isExpired())
                .orElseThrow(() -> new TokenNotFoundException("Invalid or expired verification token"));
    }

    //  ========== PASSWORD RESET TOKEN ==========

    public PasswordResetToken createPasswordResetToken(UserEntity user) {
        passwordResetTokenRepository.deleteAllByUser_Id(user.getId());
        PasswordResetToken token = PasswordResetToken.create(user, EXPIRATION_MINUTES);
        return passwordResetTokenRepository.save(token);
    }

    public PasswordResetToken validatePasswordResetToken(String token) {
        return passwordResetTokenRepository.findByToken(token)
                .filter(t -> !t.isExpired())
                .orElseThrow(() -> new TokenNotFoundException("Invalid or expired password reset token"));
    }

    public void deletePasswordResetToken(String token) {
        passwordResetTokenRepository.deleteById(token);
    }
}