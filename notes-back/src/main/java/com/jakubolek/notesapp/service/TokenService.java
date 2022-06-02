package com.jakubolek.notesapp.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface TokenService {

    void refreshToken(String authorizationHeader, HttpServletResponse response, HttpServletRequest request) throws IOException;

    String createAccessToken(String username, String request);

    String createRefreshToken(String username, String request);

    void printTokens(String accessToken, String refreshToken, HttpServletResponse response) throws IOException;

    void printError(String error, HttpServletResponse response) throws IOException;
}
