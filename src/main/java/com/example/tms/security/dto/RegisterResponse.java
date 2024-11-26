package com.example.tms.security.dto;

public record RegisterResponse(Long userId, String email, String token) {
}
