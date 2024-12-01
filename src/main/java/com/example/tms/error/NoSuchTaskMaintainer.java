package com.example.tms.error;

public class NoSuchTaskMaintainer extends RuntimeException {
    public NoSuchTaskMaintainer(String message) {
        super(message);
    }
}
