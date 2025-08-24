package com.microservice.userDetails.dto;

public record AuthResponse(String accessToken, String refreshToken) {
}
