package com.jakubolek.notesapp.utils;

public class UserDefaults {
    public static final String defaultUsername = "defaultUsername";
    public static final String defaultEmail = "defaultEmail@gmail.com";
    public static final String defaultPassword = "defaultPassword";
    public static final String defaultRole = "ROLE_ADMIN";

    public static final String defaultSecondUsername = "defaultSecondUsername";
    public static final String defaultSecondEmail = "defaultSecondEmail@gmail.com";
    public static final String defaultSecondPassword = "defaultSecondPassword";

    public static final String testRole = "ROLE_TEST";
    public static final String userRole = "ROLE_USER";
    public static final String notExistRole = "ROLE_NOT_EXIST";

    public static final String missingPermissionMessage = "You don`t have permission to this action.";
    public static final String roleAlreadyExistMessage = "Role ROLE_ADMIN already exist.";
    public static final String roleNotFoundMessage = "Role: \"ROLE_NOT_EXIST\" not found.";
    public static final String takenUsernameMessage = "This username is already taken.";
    public static final String userNotFoundMessage = "User: \"notExistUser\" not found.";

    public static final String usernameKey = "username";
    public static final String emailKey = "email";
    public static final String passwordKey = "password";
    public static final String roleKey = "role";
    public static final String noteExistUser = "notExistUser";
    public static final String authorizationKey = "authorization";
}