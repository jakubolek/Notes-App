package com.jakubolek.notesapp.service;

import com.jakubolek.notesapp.model.Role;
import com.jakubolek.notesapp.model.User;

public interface UserService {

    Role saveRole(String name);

    Role addRoleToUser(String username, String roleName);

    Role getRole(String name);

    User getUser(String username);

    User saveUser(String username, String email, String password);

}
