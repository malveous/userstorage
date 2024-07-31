package com.globallogic.userstorage.persistence.repository;

import com.globallogic.userstorage.persistence.entity.Phone;
import com.globallogic.userstorage.persistence.entity.User;
import com.globallogic.userstorage.util.UserStorageModelDataUtility;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder().name("Marcelo").email("malveous@github.com").password("C0mpl3j0!")
                .userId(UserStorageModelDataUtility.generateRandomUUIDAsString())
                .token(UserStorageModelDataUtility.generateRandomUUIDAsString()).active(true).build();
    }

    @AfterEach
    void tearDown() {
        userRepository.delete(testUser);
    }

    @Test
    @Rollback(value = false)
    void saveUserSuccessfully() {
        testUser.setPhoneList(List.of(new Phone(999887, "1", "51", testUser)));
        var savedUser = userRepository.save(testUser);
        assertTrue(savedUser.getId() > 0);
    }

    @Test
    @Rollback(value = true)
    void saveUserAttemptWithInvalidEmailLength() {
        testUser = User.builder().name("Marcelo").email(
                "malveousthisisaverylargetextomakesurethiswillthrowanexceptionbecauseitisnotsupported@github.com")
                .password("C0mpl3j0!").userId(UserStorageModelDataUtility.generateRandomUUIDAsString()).build();
        testUser.setPhoneList(List.of(new Phone(999887, "1", "51", testUser)));

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(testUser);
        });

        assertNotNull(exception);
        Throwable cause = exception.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof DataException);
        var specificException = (DataException) exception.getCause();
        assertTrue(specificException.getSQLException().getMessage().contains("Value too long for column"));
    }

    @Test
    @Rollback(value = true)
    void saveUserAttemptWithInvalidNameLength() {
        testUser = User.builder().name(
                "Marcelo is software engineer with more than 15 years of experience that loves quinoa and sparkling water, but no more than Java")
                .email("malveoust@github.com").password("C0mpl3j0!")
                .userId(UserStorageModelDataUtility.generateRandomUUIDAsString()).build();
        testUser.setPhoneList(List.of(new Phone(999887, "1", "51", testUser)));

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(testUser);
        });

        Throwable cause = exception.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof DataException);
        var specificException = (DataException) exception.getCause();
        assertTrue(specificException.getSQLException().getMessage().contains("Value too long for column"));
    }

    @Test
    @Rollback(value = true)
    void saveUserAttemptWithInvalidPasswordLength() {
        testUser = User.builder().name("Marcelo").email("malveousts@github.com").password(
                "Remember my young padawan: Fear leads to anger, anger leads to hate, hate leads to suffering. May the force be with you")
                .userId(UserStorageModelDataUtility.generateRandomUUIDAsString()).build();
        testUser.setPhoneList(List.of(new Phone(999887, "1", "51", testUser)));

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(testUser);
        });

        Throwable cause = exception.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof DataException);
        var specificException = (DataException) exception.getCause();
        assertTrue(specificException.getSQLException().getMessage().contains("Value too long for column"));
    }

    @Test
    void updateLoginDetailsSuccessfully() {
        var savedUser = userRepository.save(testUser);
        var now = Instant.now();
        var result = userRepository.updateLoginDetails(UserStorageModelDataUtility.generateRandomUUIDAsString(), now,
                now, savedUser.getId());
        assertTrue(result > 0);
    }

}
