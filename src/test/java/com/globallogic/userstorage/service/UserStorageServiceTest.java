package com.globallogic.userstorage.service;

import com.globallogic.userstorage.exceptions.MissingUserTokenException;
import com.globallogic.userstorage.exceptions.RefreshTokenGenerationException;
import com.globallogic.userstorage.exceptions.UnauthorizedUserException;
import com.globallogic.userstorage.model.PhoneDto;
import com.globallogic.userstorage.model.request.UserInput;
import com.globallogic.userstorage.persistence.entity.User;
import com.globallogic.userstorage.persistence.repository.UserRepository;
import com.globallogic.userstorage.security.util.JwtUtil;
import com.globallogic.userstorage.service.impl.UserStorageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserStorageServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private UserStorageServiceImpl userStorageService;
    private String testToken;
    private User expectedUserEntity;

    @BeforeEach
    void setUp() {
        testToken = "abc123=";
        expectedUserEntity = User.builder().name("Marcelo")
                .email("malveous@github.com")
                .password("C0mpl3j0!!")
                .userId(UUID.randomUUID().toString())
                .build();
    }

    @Test
    void testSignUpSuccessfully() {
        var targetUserInput = new UserInput("Marcelo", "malveous@github.com", "C0mpl3j0!!", null);
        var expectedUserEntity = User.builder().name(targetUserInput.getName()).email(targetUserInput.getEmail())
                .password(targetUserInput.getPassword()).build();
        when(jwtUtil.generateToken(anyString())).thenReturn(testToken);
        when(userRepository.save(any(User.class))).thenReturn(expectedUserEntity);

        var signedUpUser = userStorageService.signUp(targetUserInput);
        assertNotNull(signedUpUser);
    }

    @Test
    void testLoginSuccessfully() {
        var activeStatus = true;
        when(jwtUtil.generateToken(anyString())).thenReturn(testToken);
        when(jwtUtil.isValidToken(anyString(), anyString())).thenReturn(true);

        when(userRepository.findByTokenAndActive(testToken, activeStatus))
                .thenReturn(Optional.ofNullable(expectedUserEntity));
        when(userRepository.updateLoginDetails(anyString(), any(Instant.class), any(Instant.class), anyInt())).thenReturn(1);

        var authenticatedUser = userStorageService.login(testToken);
        assertNotNull(authenticatedUser);
    }

    @Test
    void testLoginAttemptWithNoToken() {
        MissingUserTokenException exception = assertThrows(MissingUserTokenException.class, () -> {
            userStorageService.login(null);
        });
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Token required"));
    }

    @Test
    void testLoginAttemptAndNoUserFound() {
        var activeStatus = true;
        when(userRepository.findByTokenAndActive(testToken, activeStatus))
                .thenReturn(Optional.empty());

        UnauthorizedUserException exception = assertThrows(UnauthorizedUserException.class, () -> {
            userStorageService.login(testToken);
        });

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("The user data provided for interaction is not allowed"));
    }

    @Test
    void testLoginAttemptAndTokenIsNotValid() {
        var activeStatus = true;
        when(jwtUtil.isValidToken(anyString(), anyString())).thenReturn(false);

        when(userRepository.findByTokenAndActive(testToken, activeStatus))
                .thenReturn(Optional.ofNullable(expectedUserEntity));

        UnauthorizedUserException exception = assertThrows(UnauthorizedUserException.class, () -> {
            userStorageService.login(testToken);
        });

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Token expired"));
    }

    @Test
    void testLoginAttemptAndTokenRefreshFailed() {
        var activeStatus = true;
        when(jwtUtil.isValidToken(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString())).thenReturn(testToken);

        when(userRepository.findByTokenAndActive(testToken, activeStatus))
                .thenReturn(Optional.ofNullable(expectedUserEntity));
        when(userRepository.updateLoginDetails(anyString(), any(Instant.class), any(Instant.class), anyInt())).thenReturn(0);

        RefreshTokenGenerationException exception = assertThrows(RefreshTokenGenerationException.class, () -> {
            userStorageService.login(testToken);
        });

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("The user token cannot be refreshed"));
    }

    @Test
    void testLoginAttemptAndTokenRefreshInternalDataProcessFails() {
        var activeStatus = true;
        when(jwtUtil.isValidToken(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString())).thenReturn(testToken);

        when(userRepository.findByTokenAndActive(testToken, activeStatus))
                .thenReturn(Optional.ofNullable(expectedUserEntity));
        when(userRepository.updateLoginDetails(anyString(), any(Instant.class), any(Instant.class), anyInt()))
                .thenThrow(UnsupportedOperationException.class);

        RefreshTokenGenerationException exception = assertThrows(RefreshTokenGenerationException.class, () -> {
            userStorageService.login(testToken);
        });

        assertNotNull(exception);
    }

}
