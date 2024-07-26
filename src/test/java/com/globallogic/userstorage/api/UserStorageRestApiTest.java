package com.globallogic.userstorage.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globallogic.userstorage.model.request.PhoneInput;
import com.globallogic.userstorage.model.request.UserInput;
import com.globallogic.userstorage.model.response.UserRegistrationResponse;
import com.globallogic.userstorage.service.UserStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserStorageRestApi.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserStorageRestApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserStorageService userStorageService;

    @Autowired
    private ObjectMapper objectMapper;

    UserRegistrationResponse userRegistrationResponse;
    UserInput userInput;

    @BeforeEach
    void setUp() {
        var now = Instant.now();
        userRegistrationResponse = new UserRegistrationResponse(UUID.randomUUID().toString(), now, now, now,
                UUID.randomUUID().toString(), true);
        userInput = new UserInput("Marcelo", "malveous@github.com", "C0mp13j0!!",
                List.of(new PhoneInput("999888777", "1", "51")));
    }

    @Test
    void saveUserSuccessfully() throws Exception {
        when(userStorageService.registerUser(any(UserInput.class))).thenReturn(userRegistrationResponse);
        ResultActions response = mockMvc.perform(post("/api/v1/user/userRegistration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userInput)));
        response.andDo(print()).andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.userId").isString())
                .andExpect(jsonPath("$.createdDate").exists())
                .andExpect(jsonPath("$.updatedDate").exists())
                .andExpect(jsonPath("$.lastLoginDate").exists())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.active").value(true));
    }

}
