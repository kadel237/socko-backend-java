package com.soko_backend.service;

import com.soko_backend.dto.RegisterRequest;
import com.soko_backend.entity.UserEntity;
import com.soko_backend.enums.Role;
import com.soko_backend.exception.EmailAlreadyUsedException;
import com.soko_backend.exception.UserNotfoundException;
import com.soko_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    public UserEntity register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyUsedException("Email déjà utilisé : " + request.getEmail());
        }

        UserEntity user = UserEntity.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .username(request.getUsername())
                .email(request.getEmail())
                .login(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .verified(false)
                .build();

        return userRepository.save(user);
    }
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotfoundException("Utilisateur introuvable avec l'email : " + email));
    }
}
