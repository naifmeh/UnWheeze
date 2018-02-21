package com.unwheeze.exception;

public class NoApiKeyFoundException extends Exception {
    public NoApiKeyFoundException() {
        super("Api key not found in database");
    }

    public NoApiKeyFoundException(String message) {
        super(message);
    }
}
