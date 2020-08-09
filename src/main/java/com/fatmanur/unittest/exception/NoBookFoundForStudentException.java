package com.fatmanur.unittest.exception;

public class NoBookFoundForStudentException extends RuntimeException {
    public NoBookFoundForStudentException(String message) {
        super(message);
    }
}

