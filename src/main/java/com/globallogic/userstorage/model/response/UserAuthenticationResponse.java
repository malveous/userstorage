package com.globallogic.userstorage.model.response;

import com.globallogic.userstorage.model.PhoneDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserAuthenticationResponse {

    private String userId;
    private Instant created;
    private Instant lastLogin;
    private String token;
    private boolean active;
    private String name;
    private String email;
    private String password;
    private List<PhoneDto> phones;

}
