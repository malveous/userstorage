package com.globallogic.userstorage.service.impl;

import com.globallogic.userstorage.exceptions.MissingUserTokenException;
import com.globallogic.userstorage.exceptions.RefreshTokenGenerationException;
import com.globallogic.userstorage.exceptions.UnauthorizedUserException;
import com.globallogic.userstorage.model.request.UserInput;
import com.globallogic.userstorage.model.response.UserAuthenticationResponse;
import com.globallogic.userstorage.model.response.UserRegistrationResponse;
import com.globallogic.userstorage.persistence.repository.UserRepository;
import com.globallogic.userstorage.security.util.JwtUtil;
import com.globallogic.userstorage.service.UserStorageService;
import com.globallogic.userstorage.util.UserStorageModelDataUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserStorageServiceImpl implements UserStorageService {

    @Value("${app.security.passwordHashEnabled}")
    private boolean passwordHashEnabled;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public UserRegistrationResponse signUp(UserInput userInput) {
        var now = Instant.now();
        log.info("Start user registration at: {}", now);

        if (passwordHashEnabled) {
            var rawPassword = userInput.getPassword();
            userInput.setPassword(passwordEncoder.encode(rawPassword));
        }

        var targetUserEntity = UserStorageModelDataUtility.mapUserInputToUserEntity(userInput);
        targetUserEntity.setUserId(UserStorageModelDataUtility.generateRandomUUIDAsString());
        targetUserEntity.setToken(jwtUtil.generateToken(targetUserEntity.getUserId()));
        targetUserEntity.setActive(true);
        targetUserEntity.setCreatedDate(now);
        targetUserEntity.setUpdatedDate(now);
        targetUserEntity.setLastLoginDate(now);

        var inputPhonesList = userInput.getPhones();

        if (!CollectionUtils.isEmpty(inputPhonesList)) {
            var phoneEntityList = UserStorageModelDataUtility.mapPhoneDtoListToPhoneEntityList(inputPhonesList,
                    targetUserEntity);
            targetUserEntity.setPhoneList(phoneEntityList);
        }

        log.debug("Before storing user registration data for: {}", targetUserEntity.getEmail());

        var savedUserEntity = this.userRepository.save(targetUserEntity);
        log.debug("User registration successful with user ID: {}", savedUserEntity.getUserId());

        return UserStorageModelDataUtility.mapUserEntityToUserRegistrationResponse(savedUserEntity);
    }

    @Transactional
    @Override
    public UserAuthenticationResponse login(String token) {
        if (StringUtils.isBlank(token)) {
            throw new MissingUserTokenException();
        }
        var candidateUserOptBox = userRepository.findByTokenAndActive(token, true);

        if (candidateUserOptBox.isEmpty()) {
            throw new UnauthorizedUserException();
        }

        var authenticatedUser = candidateUserOptBox.get();
        var userId = authenticatedUser.getUserId();

        if (!jwtUtil.isValidToken(token, userId)) {
            throw new UnauthorizedUserException("Token expired for the target user");
        }

        var newToken = jwtUtil.generateToken(userId);
        var isNewTokenGenerated = false;
        var now = Instant.now();

        try {
            isNewTokenGenerated = userRepository.updateLoginDetails(newToken, now, now, authenticatedUser.getId()) > 0;
        } catch (Exception e) {
            throw new RefreshTokenGenerationException(e);
        }

        if (!isNewTokenGenerated) {
            throw new RefreshTokenGenerationException();
        }
        authenticatedUser.setToken(newToken);
        authenticatedUser.setLastLoginDate(now);
        return UserStorageModelDataUtility.mapUserEntityToUserAuthenticationResponse(authenticatedUser);
    }

}
