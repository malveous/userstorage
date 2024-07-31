package com.globallogic.userstorage.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globallogic.userstorage.exceptions.model.ErrorResponse;
import com.globallogic.userstorage.model.PhoneDto;
import com.globallogic.userstorage.model.request.UserInput;
import com.globallogic.userstorage.model.response.UserRegistrationResponse;
import com.globallogic.userstorage.service.UserStorageService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserStorageRestApiTest {

    @LocalServerPort
    private int port;

    private String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private UserStorageService userStorageService;

    @Autowired
    private ObjectMapper objectMapper;

    UserRegistrationResponse userRegistrationResponse;
    UserInput userInput;

    @BeforeEach
    void setUp() {
        var now = Instant.now();
        userRegistrationResponse = new UserRegistrationResponse(UUID.randomUUID().toString(), now, now,
                UUID.randomUUID().toString(), true);
        userInput = new UserInput("Marcelo", "malveous@github.com", "C0mp13j0!!",
                List.of(new PhoneDto(999887, "1", "51")));
        url = String.format("http://localhost:%d/api/v1/user", port);
    }

    @Test
    void signUpSuccessfully() throws Exception {
        var uri = new URI(url + "/sign-up");
        var headers = new HttpHeaders();

        when(userStorageService.signUp(userInput)).thenReturn(userRegistrationResponse);
        HttpEntity<UserInput> request = new HttpEntity<>(userInput, headers);
        ResponseEntity<UserRegistrationResponse> result = this.testRestTemplate.postForEntity(uri, request,
                UserRegistrationResponse.class);

        var obtainedStatusCode = result.getStatusCode();
        var obtainedDataObject = result.getBody();
        Assertions.assertThat(obtainedStatusCode).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(obtainedDataObject).isNotNull();
    }

    @Test
    void signUpSucessfully_withoutName() throws Exception {
        var uri = new URI(url + "/sign-up");
        var headers = new HttpHeaders();
        userInput.setName(null);

        when(userStorageService.signUp(userInput)).thenReturn(userRegistrationResponse);
        HttpEntity<UserInput> request = new HttpEntity<>(userInput, headers);
        ResponseEntity<UserRegistrationResponse> result = this.testRestTemplate.postForEntity(uri, request,
                UserRegistrationResponse.class);

        var obtainedStatusCode = result.getStatusCode();
        var obtainedDataObject = result.getBody();
        Assertions.assertThat(obtainedStatusCode).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(obtainedDataObject).isNotNull();
    }

    @Test
    void signUpSucessfully_withoutPhones() throws Exception {
        var uri = new URI(url + "/sign-up");
        var headers = new HttpHeaders();
        userInput.setPhones(null);

        when(userStorageService.signUp(userInput)).thenReturn(userRegistrationResponse);
        HttpEntity<UserInput> request = new HttpEntity<>(userInput, headers);
        ResponseEntity<UserRegistrationResponse> result = this.testRestTemplate.postForEntity(uri, request,
                UserRegistrationResponse.class);

        var obtainedStatusCode = result.getStatusCode();
        var obtainedDataObject = result.getBody();
        Assertions.assertThat(obtainedStatusCode).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(obtainedDataObject).isNotNull();
    }

    @Test
    void attemptSignUp_withoutEmail() throws Exception {
        var uri = new URI(url + "/sign-up");
        var headers = new HttpHeaders();
        userInput.setEmail(null);

        HttpEntity<UserInput> request = new HttpEntity<>(userInput, headers);
        ResponseEntity<List<ErrorResponse>> result = this.testRestTemplate.exchange(uri, HttpMethod.POST, request,
                new ParameterizedTypeReference<List<ErrorResponse>>() {
                });

        var obtainedStatusCode = result.getStatusCode();
        Assertions.assertThat(obtainedStatusCode).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void attemptSignUp_withInvalidEmail() throws Exception {
        var uri = new URI(url + "/sign-up");
        var headers = new HttpHeaders();
        userInput.setEmail("wrongEmail");

        HttpEntity<UserInput> request = new HttpEntity<>(userInput, headers);
        ResponseEntity<List<ErrorResponse>> result = this.testRestTemplate.exchange(uri, HttpMethod.POST, request,
                new ParameterizedTypeReference<List<ErrorResponse>>() {
                });

        var obtainedStatusCode = result.getStatusCode();
        Assertions.assertThat(obtainedStatusCode).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void attemptSignUp_withoutPassword() throws Exception {
        var uri = new URI(url + "/sign-up");
        var headers = new HttpHeaders();
        userInput.setPassword(null);

        HttpEntity<UserInput> request = new HttpEntity<>(userInput, headers);
        ResponseEntity<List<ErrorResponse>> result = this.testRestTemplate.exchange(uri, HttpMethod.POST, request,
                new ParameterizedTypeReference<List<ErrorResponse>>() {
                });

        var obtainedStatusCode = result.getStatusCode();
        Assertions.assertThat(obtainedStatusCode).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void attemptSignUp_withNoInvalidPassword() throws Exception {
        var uri = new URI(url + "/sign-up");
        var headers = new HttpHeaders();
        userInput.setPassword("1234");

        HttpEntity<UserInput> request = new HttpEntity<>(userInput, headers);
        ResponseEntity<List<ErrorResponse>> result = this.testRestTemplate.exchange(uri, HttpMethod.POST, request,
                new ParameterizedTypeReference<List<ErrorResponse>>() {
                });

        var obtainedStatusCode = result.getStatusCode();
        Assertions.assertThat(obtainedStatusCode).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
