package com.jakubolek.notesapp.controller;

import com.jakubolek.notesapp.exception.impl.MissingElementInRequestBodyException;
import com.jakubolek.notesapp.model.Role;
import com.jakubolek.notesapp.model.User;
import com.jakubolek.notesapp.service.TokenService;
import com.jakubolek.notesapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;

    @ResponseStatus(CREATED)
    @PostMapping("/users")
    public User register(@RequestBody @Valid RegistrationRequest request) {
        return userService.saveUser(request.getUsername(), request.getEmail(), request.getPassword());
    }

    @ResponseStatus(CREATED)
    @PostMapping("/roles")
    public Role saveRole(@RequestBody NewRoleRequest request) {
        return userService.saveRole(request.getRole());
    }

    @PostMapping("/users/roles")
    public Role addRoleToUser(@RequestBody RoleToUserRequest request) {
        return userService.addRoleToUser(request.getUsername(), request.getRole());
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            tokenService.refreshToken(request.getHeader(AUTHORIZATION), response, request);
        } catch (Exception e) {
            throw new MissingElementInRequestBodyException();
        }
    }
}
