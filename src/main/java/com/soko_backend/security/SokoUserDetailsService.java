package com.soko_backend.security;


import com.soko_backend.entity.user.UserEntity;
import com.soko_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SokoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmailIgnoreCase(email)
                .map(this::createSecurityUser)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " could not be found."));
    }

    private User createSecurityUser(UserEntity userEntity) {
        List<SimpleGrantedAuthority> grantedRoles = List.of(
            new SimpleGrantedAuthority( userEntity.getRole().name())
        );
        System.out.println("Rôles injectés dans UserDetails : " + grantedRoles);
        return new User(userEntity.getEmail(), userEntity.getPassword(), grantedRoles);
    }
}