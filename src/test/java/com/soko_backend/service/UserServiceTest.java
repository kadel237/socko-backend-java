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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
 void shouldSavedUser(){
        //Given
        RegisterRequest request = new RegisterRequest();
        request.setName("Elisabeth");
        request.setSurname("Ngue");
        request.setEmail("elisabeth@example.com");
        request.setPassword("password123");

        String encodedPassword = "encodedPassword123";
        when(passwordEncoder.encode("password123")).thenReturn(encodedPassword);

        UserEntity savedUser = new UserEntity();
        savedUser.setId(1L);
        savedUser.setName("Elisabeth");
        savedUser.setSurname("Ngue");
        savedUser.setEmail("elisabeth@example.com");
        savedUser.setPassword(encodedPassword);
        savedUser.setRole(Role.CUSTOMER);
        savedUser.setLogin("elisabeth@example.com");

        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        // when
        UserEntity result = userService.register(request);

        // then
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        UserEntity capturedUser = userCaptor.getValue();
        assertEquals("Elisabeth", capturedUser.getName());
        assertEquals("Ngue", capturedUser.getSurname());
        assertEquals("elisabeth@example.com", capturedUser.getEmail());
        assertEquals(encodedPassword, capturedUser.getPassword());
        assertEquals(Role.CUSTOMER, capturedUser.getRole());
        assertEquals("elisabeth@example.com", capturedUser.getLogin());

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }


    }

