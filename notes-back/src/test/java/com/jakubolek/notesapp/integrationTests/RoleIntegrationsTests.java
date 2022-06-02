package com.jakubolek.notesapp.integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakubolek.notesapp.model.Role;
import com.jakubolek.notesapp.model.User;
import com.jakubolek.notesapp.repository.RoleRepository;
import com.jakubolek.notesapp.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static com.jakubolek.notesapp.utils.ApiUrlDefaults.*;
import static com.jakubolek.notesapp.utils.UserDefaults.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class RoleIntegrationsTests {

    private String accessToken;
    private User defaultUser;

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeAll
    void setUp() throws Exception {
        createNewUserAndRole();
        accessToken = signIn();
        defaultUser = initDefaultUser();
    }

    @Test
    void shouldCreateNewRole() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(createRoleUrl)
                .contentType(APPLICATION_JSON)
                .header(authorizationKey, accessToken)
                .content(objectMapper.writeValueAsString(contentBody(testRole))))
                .andReturn();

        Role role = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Role.class);

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(201);
        assertThat(role.getName()).isEqualTo(testRole);
        assertThat(roleRepository.findByName(testRole)).isNotNull();

    }

    @Test
    void shouldNotCreateNewRoleWhenRoleNameExist() throws Exception {
        long rolesQuantityBeforeTest = roleRepository.count();

        MvcResult mvcResult = mockMvc.perform(post(createRoleUrl)
                .contentType(APPLICATION_JSON)
                .header(authorizationKey, accessToken)
                .content(objectMapper.writeValueAsString(contentBody(defaultRole))))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(409);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(roleAlreadyExistMessage);
        assertThat(rolesQuantityBeforeTest).isEqualTo(roleRepository.count());
    }

    @Test
    void shouldNotCreateNewRoleWhenRequestBodyIsNotCorrect() throws Exception {
        long rolesQuantityBeforeTest = roleRepository.count();

        MvcResult mvcResult = mockMvc.perform(post(createRoleUrl)
                .contentType(APPLICATION_JSON)
                .header(authorizationKey, accessToken)
                .content("incorrect content"))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
        assertThat(rolesQuantityBeforeTest).isEqualTo(roleRepository.count());
    }

    @Test
    void shouldAddNewRoleToUser() throws Exception {
        createNewUser();
        createNewRole();

        MvcResult mvcResult = mockMvc.perform(post(addRoleToUserUrl)
                .contentType(APPLICATION_JSON)
                .header(authorizationKey, accessToken)
                .content(objectMapper.writeValueAsString(contentBodyToAddRole(defaultSecondUsername, userRole))))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        assertThat(defaultUser.getRoles().size()).isEqualTo(1);
    }

    @Test
    void shouldNotAddRoleToUserThatDoesNotExist() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(addRoleToUserUrl)
                .contentType(APPLICATION_JSON)
                .header(authorizationKey, accessToken)
                .content(objectMapper.writeValueAsString(contentBodyToAddRole(noteExistUser, defaultRole))))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(404);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(userNotFoundMessage);
    }

    @Test
    void shouldNotAddRoleToUserWhenRoleDoesNotExist() throws Exception {
        long rolesQuantityBeforeTest = defaultUser.getRoles().size();

        MvcResult mvcResult = mockMvc.perform(post(addRoleToUserUrl)
                .contentType(APPLICATION_JSON)
                .header(authorizationKey, accessToken)
                .content(objectMapper.writeValueAsString(contentBodyToAddRole(defaultUsername, notExistRole))))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(404);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(roleNotFoundMessage);
        assertThat(rolesQuantityBeforeTest).isEqualTo(defaultUser.getRoles().size());
    }

    @Test
    void shouldNotAddRoleToUserRequestBodyIsNotCorrect() throws Exception {
        long rolesQuantityBeforeTest = defaultUser.getRoles().size();

        MvcResult mvcResult = mockMvc.perform(post(addRoleToUserUrl)
                .contentType(APPLICATION_JSON)
                .header(authorizationKey, accessToken)
                .content("incorrect content"))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
        assertThat(rolesQuantityBeforeTest).isEqualTo(defaultUser.getRoles().size());
    }

    private void createNewUserAndRole() {
        userService.saveRole(defaultRole);
        userService.saveUser(defaultUsername, defaultEmail, defaultPassword);
        userService.addRoleToUser(defaultUsername, defaultRole);
    }

    private void createNewRole() {
        userService.saveRole(userRole);
    }

    private void createNewUser() {
        userService.saveUser(defaultSecondUsername, defaultSecondEmail, defaultSecondPassword);
    }

    private String signIn() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(loginUrl)
                .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                .param(usernameKey, defaultUsername)
                .param(passwordKey, defaultPassword))
                .andReturn();

        Map resultString = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Map.class);
        return "Bearer " + resultString.get("access_token");
    }

    private User initDefaultUser() {
        return userService.getUser(defaultUsername);
    }

    private Map<String, String> contentBody(String value) {
        Map<String, String> payload = new HashMap<>();
        payload.put(roleKey, value);
        return payload;
    }

    private Map<String, String> contentBodyToAddRole(String username, String role) {
        Map<String, String> payload = new HashMap<>();
        payload.put(usernameKey, username);
        payload.put(roleKey, role);
        return payload;
    }
}