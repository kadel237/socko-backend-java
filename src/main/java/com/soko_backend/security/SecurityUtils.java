package com.soko_backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {


    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;


    public static final String AUTHORITIES_CLAIM_KEY = "auth";
}