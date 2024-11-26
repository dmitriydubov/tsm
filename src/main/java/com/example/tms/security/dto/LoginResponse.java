package com.example.tms.security.dto;

import java.util.List;

public record LoginResponse(Long id, String token, String refreshToken, String email, List<String> roles) {
}
