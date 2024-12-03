package com.example.tms.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

class SecurityControllerTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser(username = "admin1@email.com", password = "pass", roles = "[\"ROLE_ADMIN\"]")
    void signIn() throws Exception {

    }

    @Test
    void signUp() {
    }

    @Test
    void refreshToken() {
    }
}