package com.globallogic.userstorage.api;

import com.globallogic.userstorage.model.request.TokenAuthenticationInput;
import com.globallogic.userstorage.model.request.UserInput;
import com.globallogic.userstorage.model.response.UserAuthenticationResponse;
import com.globallogic.userstorage.model.response.UserRegistrationResponse;
import com.globallogic.userstorage.service.UserStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserStorageRestApi {

    private final UserStorageService userStorageService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserRegistrationResponse> signUp(@Valid @RequestBody UserInput userInput) {
        return ResponseEntity.ok(this.userStorageService.signUp(userInput));
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthenticationResponse> login(
            @RequestBody(required = true) TokenAuthenticationInput tokenAuthenticationInput) {
        return ResponseEntity.ok(this.userStorageService.login(tokenAuthenticationInput.getToken()));
    }

}
