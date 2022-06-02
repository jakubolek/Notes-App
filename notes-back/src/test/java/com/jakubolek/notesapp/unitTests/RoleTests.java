package com.jakubolek.notesapp.unitTests;

import com.jakubolek.notesapp.model.Role;
import com.jakubolek.notesapp.model.User;
import com.jakubolek.notesapp.repository.RoleRepository;
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
class RoleTests {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeAll
    void setUp() {
        createNewUserAndRole();
    }

    @Test
    void shouldCreateNewRole() {
        userService.saveRole(testRole);
        Role role = roleRepository.findByName(testRole);

        assertThat(role.getName()).isEqualTo(testRole);
    }

    @Test
    void shouldNotCreateNewRoleWhenRoleNameExist() {
        long rolesQuantityBeforeTest = roleRepository.count();

        Exception exception = assertThrows(RuntimeException.class, () ->
                userService.saveRole(defaultRole));

        assertThat(exception.getMessage()).isEqualTo(roleAlreadyExistMessage);
        assertThat(rolesQuantityBeforeTest).isEqualTo(roleRepository.count());
    }

    @Test
    void shouldAddRoleToUser() {
        createNewUser();
        createNewRole();

        userService.addRoleToUser(defaultSecondUsername, userRole);

        User user = userService.getUser(defaultSecondUsername);

        assertThat(user.getRoles().size()).isEqualTo(1);
    }

    @Test
    void shouldNotAddRoleToUserThatNotExist() {
        Exception exception = assertThrows(RuntimeException.class, () ->
                userService.addRoleToUser(noteExistUser, defaultRole));

        assertThat(exception.getMessage()).isEqualTo(userNotFoundMessage);
    }

    @Test
    void shouldNotAddRoleToUserWhenRoleDoesNotExist() {
        User user = userService.getUser(defaultUsername);

        long rolesQuantityBeforeTest = user.getRoles().size();

        Exception exception = assertThrows(RuntimeException.class, () ->
                userService.addRoleToUser(defaultUsername, notExistRole));

        assertThat(exception.getMessage()).isEqualTo(roleNotFoundMessage);
        assertThat(rolesQuantityBeforeTest).isEqualTo(user.getRoles().size());
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
}
