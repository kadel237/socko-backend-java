package com.soko_backend.security;

import com.soko_backend.entity.UserEntity;
import com.soko_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
@RequiredArgsConstructor
public class SecurityUtils {


    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;


    public static final String AUTHORITIES_CLAIM_KEY = "auth";
}