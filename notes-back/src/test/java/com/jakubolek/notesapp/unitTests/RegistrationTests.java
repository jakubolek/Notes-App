package com.jakubolek.notesapp.unitTests;

import com.jakubolek.notesapp.model.User;
import com.jakubolek.notesapp.repository.UserRepository;
import com.jakubolek.notesapp.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.jakubolek.notesapp.utils.UserDefaults.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void setUp() {
        createNewUser();
    }

    @Test
    void shouldCreateNewUser() {
        userService.saveUser(defaultSecondUsername, defaultSecondEmail, defaultSecondPassword);

        User user = userRepository.findByUsername(defaultSecondUsername);
        assertThat(user.getUsername()).isEqualTo(defaultSecondUsername);
        assertThat(user.getEmail()).isEqualTo(defaultSecondEmail);
    }

    @Test
    void shouldNotCreateUserWhenUsernameIsTaken() {
        long usersQuantityBeforeTest = userRepository.count();

        Exception exception = assertThrows(RuntimeException.class, () ->
                userService.saveUser(defaultUsername, defaultEmail, defaultPassword));

        assertThat(exception.getMessage()).isEqualTo(takenUsernameMessage);
        assertThat(usersQuantityBeforeTest).isEqualTo(userRepository.count());
    }

    private void createNewUser() {
        userService.saveUser(defaultUsername, defaultEmail, defaultPassword);
    }
}
