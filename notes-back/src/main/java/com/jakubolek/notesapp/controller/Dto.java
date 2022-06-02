package com.jakubolek.notesapp.controller;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
class RegistrationRequest {

    @NotBlank
    @Size(min = 4, max = 30)
    private String username;

    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    @NotBlank
    @Size(min = 4, max = 30)
    private String password;
}

@Data
class NewRoleRequest {
    private String role;
}

@Data
class RoleToUserRequest {
    private String username;
    private String role;

}

@Data
class CreateNoteRequest {
    private String note;
}

@Data
class UpdateNoteRequest {
    private String text;
}