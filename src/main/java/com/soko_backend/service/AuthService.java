package com.soko_backend.service;

import com.soko_backend.dto.AuthResponse;
import com.soko_backend.dto.LoginRequest;
import com.soko_backend.dto.RegisterRequest;
import com.soko_backend.entity.User;
import com.soko_backend.enums.Role;
import com.soko_backend.exception.EmailAlreadyUsedException;
import com.soko_backend.exception.InvalidCredentialsException;
import com.soko_backend.exception.UserNotfoundException;
import com.soko_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Service
public class AuthService {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MailService mailService;

    public AuthResponse register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyUsedException(request.getEmail());
        }
        User user = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token,user.getRole().name(), user.getEmail(),user.getName());
    }

    public AuthResponse login(LoginRequest request){

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(UserNotfoundException::new);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){

            throw new InvalidCredentialsException(user.getName());
        }
        String token = jwtService.generateToken(user);
        return new AuthResponse(token,user.getRole().name(), user.getEmail(), user.getName());

    }




}
