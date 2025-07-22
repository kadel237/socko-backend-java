package com.soko_backend.controller;

import com.soko_backend.dto.auth.LoginRequest;
import com.soko_backend.dto.auth.RegisterRequest;
import com.soko_backend.dto.auth.UserAuthentication;
import com.soko_backend.dto.password.ForgotPasswordRequest;
import com.soko_backend.dto.password.ResetpasswordRequest;
import com.soko_backend.entity.PasswordResetToken;
import com.soko_backend.entity.UserEntity;
import com.soko_backend.entity.VerificationToken;
import com.soko_backend.repository.UserRepository;
import com.soko_backend.security.JwtService;
import com.soko_backend.service.MailService;
import com.soko_backend.service.TokenService;
import com.soko_backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Accounts API")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;
    @Autowired
    MailService mailService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService , TokenService tokenService, MailService mailService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService=tokenService;
        this.mailService=mailService;
    }
    @Autowired
    private JwtService jwtService;

    private final SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();

    @Operation(summary = "Authenticates user", description = "Authenticates user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is logged in.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserAuthentication.class))}),
            @ApiResponse(responseCode = "403", description = "User credentials are not valid."),
            @ApiResponse(responseCode = "400", description = "Login or password is not provided.")
    })
    @PostMapping("/login")
    public ResponseEntity<UserAuthentication> login(@RequestBody @Valid LoginRequest credentials) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(credentials.email(), credentials.password());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        String jwt = jwtService.createToken(authentication);
        return new ResponseEntity<>(
                new UserAuthentication(authentication.getName(), jwt),
                HttpStatus.OK
        );
    }
    @Operation(summary = "Register user", description = "Register a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User is created.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserAuthentication.class))}),
            @ApiResponse(responseCode = "403", description = "User credentials are not valid."),
            @ApiResponse(responseCode = "400", description = "Login or password is not provided.")
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        // Enregistrement du user
        UserEntity user = userService.register(request);

        // Génération du token de vérification
        VerificationToken token = tokenService.createVerificationToken(user);

        // Lien de vérification
        String link = "http://localhost:8080/api/auth/verify?token=" + token.getToken();

        // Envoi de l’email
        mailService.sendVerificationEmail(user.getEmail(), link);

        return ResponseEntity.ok("Inscription réussie. Vérifiez votre email pour activer le compte.");
    }
    @Operation(summary = "Vérifier un compte utilisateur via le token reçu par email")
    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        VerificationToken verificationToken = tokenService.validateVerificationToken(token);
        UserEntity user = verificationToken.getUser();
        user.setVerified(true);

        userService.save(user);  // <- méthode save(UserEntity) dans UserService

        return ResponseEntity.ok("Compte vérifié avec succès !");
    }
    @Operation(summary = "Envoyer un lien de réinitialisation de mot de passe")
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        UserEntity user = userService.findByEmail(request.getEmail());

        PasswordResetToken token = tokenService.createPasswordResetToken(user);
        String resetLink = "http://localhost:8080/reset-password?token=" + token.getToken();

        mailService.sendPasswordResetEmail(user.getEmail(), resetLink);

        return ResponseEntity.ok("Un lien de réinitialisation a été envoyé à votre email.");
    }
    @Operation(summary = "Réinitialiser le mot de passe via token")
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam("token") String token,
            @RequestBody ResetpasswordRequest request) {

        PasswordResetToken resetToken = tokenService.validatePasswordResetToken(token);
        UserEntity user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userService.save(user);

        tokenService.deletePasswordResetToken(token);

        return ResponseEntity.ok("Mot de passe mis à jour avec succès.");
    }
}
