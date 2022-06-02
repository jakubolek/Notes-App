package com.jakubolek.notesapp.integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakubolek.notesapp.model.User;
import com.jakubolek.notesapp.repository.UserRepository;
import com.jakubolek.notesapp.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static com.jakubolek.notesapp.utils.ApiUrlDefaults.registrationUrl;
import static com.jakubolek.notesapp.utils.UserDefaults.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeAll
    void setUp() {
        createNewUser();
    }

    @Test
    void shouldCreateNewUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(registrationUrl)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payloadToCreateUser(
                        defaultSecondUsername,
                        defaultSecondEmail,
                        defaultEmail))))
                .andReturn();

        User user = userRepository.findByUsername(defaultUsername);

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(201);
        assertThat(user.getUsername()).isEqualTo(defaultUsername);
        assertThat(user.getEmail()).isEqualTo(defaultEmail);
    }

    @Test
    void shouldNotCreateUserWhenUsernameIsTaken() throws Exception {
        long usersQuantityBeforeTest = userRepository.count();

        MvcResult mvcResult = mockMvc.perform(post(registrationUrl)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payloadToCreateUser(
                        defaultUsername,
                        defaultEmail,
                        defaultPassword))))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(409);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(takenUsernameMessage);
        assertThat(userRepository.findByUsername(defaultSecondUsername)).isNotNull();
        assertThat(usersQuantityBeforeTest).isEqualTo(userRepository.count());
    }

    @Test
    void shouldNotCreateUserWhenRequestBodyIsNotCorrect() throws Exception {
        long usersQuantityBeforeTest = userRepository.count();

        MvcResult mvcResult = mockMvc.perform(post(registrationUrl)
                .contentType(APPLICATION_JSON)
                .content("incorrect content"))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
        assertThat(usersQuantityBeforeTest).isEqualTo(userRepository.count());
    }

    private void createNewUser() {
        userService.saveUser(defaultUsername, defaultEmail, defaultPassword);
    }

    private Map<String, String> payloadToCreateUser(String username, String email, String password) {
        Map<String, String> payload = new HashMap<>();
        payload.put(usernameKey, username);
        payload.put(emailKey, email);
        payload.put(passwordKey, password);
        return payload;
    }
}