package com.soko_backend.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResendMailService implements MailService {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${resend.from.email}")
    private String from;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String resendUrl = "https://api.resend.com/emails";

    @Override
    public void sendVerificationEmail(String to, String verificationLink) {
        String subject = "Vérification de votre adresse email";
        String html = "<p>Merci de vous être inscrit !</p>" +
                "<p>Cliquez sur le lien ci-dessous pour vérifier votre adresse :</p>" +
                "<a href=\"" + verificationLink + "\">Vérifier mon compte</a>";
        sendEmail(to, subject, html);
    }

    @Override
    public void sendPasswordResetEmail(String to, String resetLink) {
        String subject = "Réinitialisation de votre mot de passe";
        String html = "<p>Vous avez demandé à réinitialiser votre mot de passe.</p>" +
                "<p>Cliquez ici :</p>" +
                "<a href=\"" + resetLink + "\">Réinitialiser mon mot de passe</a>";
        sendEmail(to, subject, html);
    }

    private void sendEmail(String to, String subject, String html) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, Object> body = Map.of(
                "from", from,
                "to", to,
                "subject", subject,
                "html", html
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(resendUrl, request, String.class);
    }
}