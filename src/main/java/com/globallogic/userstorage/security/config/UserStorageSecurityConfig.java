package com.globallogic.userstorage.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserStorageSecurityConfig {

    @Value("${app.security.bcrypt.strength}")
    private int bCryptStrength;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(bCryptStrength);
    }

}
