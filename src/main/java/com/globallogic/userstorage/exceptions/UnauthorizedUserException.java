package com.globallogic.userstorage.exceptions;

public class UnauthorizedUserException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "The user data provided for interaction is not allowed. Make sure user data is either registered or valid";

    public UnauthorizedUserException() {
        super(DEFAULT_MESSAGE);
    }

    public UnauthorizedUserException(String message) {
        super(message);
    }

    public UnauthorizedUserException(Throwable e) {
        super(e);
    }

    public UnauthorizedUserException(String message, Throwable e) {
        super(message, e);
    }

}
