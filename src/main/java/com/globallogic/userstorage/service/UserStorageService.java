package com.globallogic.userstorage.service;

import com.globallogic.userstorage.model.request.UserInput;
import com.globallogic.userstorage.model.response.UserRegistrationResponse;

public interface UserStorageService {

    UserRegistrationResponse registerUser(UserInput userInput);

}
