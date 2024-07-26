package com.globallogic.userstorage.api;

import com.globallogic.userstorage.model.request.UserInput;
import com.globallogic.userstorage.model.response.UserRegistrationResponse;
import com.globallogic.userstorage.service.UserStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserStorageRestApi {

    private final UserStorageService userStorageService;

    @PostMapping("/userRegistration")
    public ResponseEntity<UserRegistrationResponse> registerUser(@Valid @RequestBody UserInput userInput) {
        return ResponseEntity.ok(this.userStorageService.registerUser(userInput));
    }

}
