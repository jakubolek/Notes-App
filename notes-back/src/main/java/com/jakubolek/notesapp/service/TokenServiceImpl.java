package com.jakubolek.notesapp.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakubolek.notesapp.exception.impl.UnauthorizedException;
import com.jakubolek.notesapp.infrastructure.JwtProperties;
import com.jakubolek.notesapp.model.Role;
import com.jakubolek.notesapp.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final UserService userService;
    private final JwtProperties jwt;

    @Override
    public String createAccessToken(String username, String request) {
        Algorithm algorithm = Algorithm.HMAC256(jwt.secret.getBytes());
        User user = userService.getUser(username);

        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwt.accessTokenExpireTime))
                .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .withIssuer(request)
                .sign(algorithm);
    }

    @Override
    public String createRefreshToken(String username, String request) {
        Algorithm algorithm = Algorithm.HMAC256(jwt.secret.getBytes());
        User user = userService.getUser(username);

        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwt.refreshTokenExpireTime))
                .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .withIssuer(request)
                .sign(algorithm);
    }

    @Override
    public void printTokens(String accessToken, String refreshToken, HttpServletResponse response) throws IOException {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    @Override
    public void printError(String errorMessage, HttpServletResponse response) throws IOException {
        Map<String, String> error = new HashMap<>();
        error.put("error_message", errorMessage);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }

    @Override
    public void refreshToken(String authorizationHeader, HttpServletResponse response, HttpServletRequest request) throws IOException {

        if (authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(jwt.secret.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);

                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);

                String httpRequest = request.getRequestURL().toString();
                String accessToken = createAccessToken(user.getUsername(), httpRequest);

                printTokens(accessToken, refreshToken, response);

            } catch (Exception e) {
                printError(e.getMessage(), response);
            }

        } else {
            throw new UnauthorizedException();
        }
    }
}
