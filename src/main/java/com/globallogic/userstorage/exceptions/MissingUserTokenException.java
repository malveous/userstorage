package com.globallogic.userstorage.exceptions;

public class MissingUserTokenException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Token required for interaction with the system is missing";

    public MissingUserTokenException() {
        super(DEFAULT_MESSAGE);
    }

    public MissingUserTokenException(String message) {
        super(message);
    }

    public MissingUserTokenException(Throwable e) {
        super(e);
    }

    public MissingUserTokenException(String message, Throwable e) {
        super(message, e);
    }

}
