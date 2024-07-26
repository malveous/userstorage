package com.globallogic.userstorage.service.impl;

import com.globallogic.userstorage.model.request.UserInput;
import com.globallogic.userstorage.model.response.UserRegistrationResponse;
import com.globallogic.userstorage.persistence.repository.UserRepository;
import com.globallogic.userstorage.service.UserStorageService;
import com.globallogic.userstorage.util.UserStorageModelDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserStorageServiceImpl implements UserStorageService {

    @Value("${app.security.passwordHashEnabled}")
    private boolean passwordHashEnabled;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public UserRegistrationResponse registerUser(UserInput userInput) {
        var now = Instant.now();
        log.info("Start user registration at: {}", now);

        if (passwordHashEnabled) {
            var rawPassword = userInput.getPassword();
            userInput.setPassword(passwordEncoder.encode(rawPassword));
        }

        var targetUserEntity = UserStorageModelDataMapper.mapUserInputToUserEntity(userInput);
        targetUserEntity.setActive(true);
        targetUserEntity.setCreatedDate(now);
        targetUserEntity.setUpdatedDate(now);
        targetUserEntity.setLastLoginDate(now);

        var phoneEntityList = UserStorageModelDataMapper.mapPhoneInputToPhoneEntity(userInput.getPhones(),
                targetUserEntity);
        targetUserEntity.setPhoneList(phoneEntityList);
        log.debug("Before storing user registration data for: {}", targetUserEntity.getEmail());

        var savedUserEntity = this.userRepository.save(targetUserEntity);
        log.debug("User registration successful with user ID: {}", savedUserEntity.getUserId());

        return UserStorageModelDataMapper.mapUserEntityToUserResponse(savedUserEntity);
    }
}
