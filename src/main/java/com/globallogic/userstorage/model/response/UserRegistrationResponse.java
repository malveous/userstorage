package com.globallogic.userstorage.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRegistrationResponse {

    private String userId;
    private Instant created;
    private Instant lastLogin;
    private String token;
    private boolean active;

}
