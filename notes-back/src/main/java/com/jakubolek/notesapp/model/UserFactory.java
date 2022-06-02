package com.jakubolek.notesapp.model;

import java.util.Collections;

public class UserFactory {

    public static User create(String username, String email, String password, Role role) {
        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .roles(Collections.singleton(role))
                .build();
    }
}
