package com.globallogic.userstorage.service;

import com.globallogic.userstorage.model.request.UserInput;
import com.globallogic.userstorage.model.response.UserAuthenticationResponse;
import com.globallogic.userstorage.model.response.UserRegistrationResponse;

public interface UserStorageService {

    UserRegistrationResponse signUp(UserInput userInput);

    UserAuthenticationResponse login(String token);

}
