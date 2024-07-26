package com.globallogic.userstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class UserstorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserstorageApplication.class, args);
    }

}
