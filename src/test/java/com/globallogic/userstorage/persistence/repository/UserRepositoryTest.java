package com.globallogic.userstorage.persistence.repository;

import com.globallogic.userstorage.persistence.entity.Phone;
import com.globallogic.userstorage.persistence.entity.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Test 1: Save user successfully")
    @Order(1)
    @Rollback(value = false)
    void saveUserSuccessfully() {
        var user = User.builder().name("Marcelo").email("malveous@github.com").password("C0mpl3j0!").build();
        user.setPhoneList(List.of(new Phone("999888776", "1", "51", user)));
        userRepository.save(user);
        assertTrue(user.getId() > 0);
        assertNotNull(user.getUserId());
        assertNotNull(user.getToken());
    }

    @Test
    @DisplayName("Test 2: Save user attempt with invalid email length")
    @Order(2)
    @Rollback(value = true)
    void saveUserAttemptWithInvalidEmailLength() {
        var user = User.builder().name("Marcelo").email(
                "malveousthisisaverylargetextomakesurethiswillthrowanexceptionbecauseitisnotsupported@github.com")
                .password("C0mpl3j0!").build();
        user.setPhoneList(List.of(new Phone("999888776", "1", "51", user)));

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user);
        });

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Value too long for column"));
    }

    @Test
    @DisplayName("Test 3: Save user attempt with invalid name length")
    @Order(3)
    @Rollback(value = true)
    void saveUserAttemptWithInvalidNameLength() {
        var user = User.builder().name(
                "Marcelo is software engineer with more than 15 years of experience that loves quinoa and sparkling water, but no more than Java")
                .email("malveoust@github.com").password("C0mpl3j0!").build();
        user.setPhoneList(List.of(new Phone("999888776", "1", "51", user)));

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user);
        });

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Value too long for column"));
    }

    @Test
    @DisplayName("Test 4: Save user attempt with invalid password length")
    @Order(4)
    @Rollback(value = true)
    void saveUserAttemptWithInvalidPasswordLength() {
        var user = User.builder().name("Marcelo").email("malveousts@github.com").password(
                "Remember my young padawan: Fear leads to anger, anger leads to hate, hate leads to suffering. May the force be with you")
                .build();
        user.setPhoneList(List.of(new Phone("999888776", "1", "51", user)));

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user);
        });

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Value too long for column"));
    }

}
