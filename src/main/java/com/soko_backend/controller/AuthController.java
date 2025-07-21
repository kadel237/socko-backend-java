package com.soko_backend.controller;

import com.soko_backend.dto.LoginRequest;
import com.soko_backend.dto.RegisterRequest;
import com.soko_backend.dto.UserAuthentication;
import com.soko_backend.entity.UserEntity;
import com.soko_backend.repository.UserRepository;
import com.soko_backend.security.JwtService;
import com.soko_backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
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
    public ResponseEntity<UserEntity> register(@RequestBody RegisterRequest credentials) {
        if (credentials.getEmail() == null || credentials.getPassword() == null) {
            return ResponseEntity.badRequest().build();
        }

        UserEntity savedUser = userService.register(credentials);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);


    }
}
