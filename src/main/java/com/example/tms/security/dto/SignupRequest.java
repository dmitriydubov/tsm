package com.example.tms.security.dto;

import com.example.tms.security.model.Role;

import java.util.Set;

public record SignupRequest(String email, String username, String password, Set<Role> roles) {
}
