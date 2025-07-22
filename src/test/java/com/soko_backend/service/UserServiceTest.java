package com.soko_backend.service;

import com.soko_backend.dto.RegisterRequest;
import com.soko_backend.entity.UserEntity;
import com.soko_backend.enums.Role;
import com.soko_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void shouldSaveUser() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("elisabeth@example.com");
        request.setName("Elisabeth");
        request.setSurname("Georgia");
        request.setPassword("securePassword");

        UserEntity savedUser = new UserEntity();
        savedUser.setEmail(request.getEmail());
        savedUser.setName(request.getName());
        savedUser.setSurname(request.getSurname());
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(Role.CUSTOMER);
        savedUser.setLogin(request.getEmail());

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        UserEntity result = userService.register(request);

        assertEquals("elisabeth@example.com", result.getEmail());
        assertEquals("Elisabeth", result.getName());
        assertEquals("Georgia", result.getSurname());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(Role.CUSTOMER, result.getRole());
    }
    @Test
    public void shouldRetournUserMail(){

        // Given
        String email = "test@example.com";
        UserEntity expectedUser = new UserEntity();
        expectedUser.setEmail(email);
        expectedUser.setName("Test");
        expectedUser.setRole(Role.CUSTOMER);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        // When
        Optional<UserEntity> actual = Optional.ofNullable(userService.findByEmail(email));

        // Then
        assertTrue(actual.isPresent());
        assertEquals(expectedUser.getEmail(), actual.get().getEmail());
        verify(userRepository, times(1)).findByEmail(email);

    }


    }

