package com.example.tms.dto;

import java.util.List;

public record TaskRequest(
        String title,
        String description,
        String priority,
        List<String> comments,
        String author,
        String contractor) {}
