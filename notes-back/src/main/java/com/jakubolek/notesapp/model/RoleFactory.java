package com.jakubolek.notesapp.model;

public class RoleFactory {

    public static Role create(String roleName) {
        return Role.builder()
                .name(roleName)
                .build();
    }
}
