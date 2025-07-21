package com.soko_backend.service;

import com.soko_backend.dto.RegisterRequest;
import com.soko_backend.entity.UserEntity;
import com.soko_backend.enums.Role;
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
        UserEntity user = new UserEntity();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);
        user.setLogin(request.getEmail());

        return userRepository.save(user);
    }
}
