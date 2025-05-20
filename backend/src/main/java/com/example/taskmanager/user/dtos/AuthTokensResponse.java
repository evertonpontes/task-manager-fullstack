package com.example.taskmanager.user.dtos;

public record AuthTokensResponse(
    String accessToken,
    String sessionToken
) { }
