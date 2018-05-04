package com.jarq.system.exceptions;

public class ServiceException extends Exception {

    private final String message;

    public ServiceException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
