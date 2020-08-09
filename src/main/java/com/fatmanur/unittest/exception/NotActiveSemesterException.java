package com.fatmanur.unittest.exception;

public class NotActiveSemesterException extends RuntimeException {

    public NotActiveSemesterException(String message) {
        super(message);
    }
}