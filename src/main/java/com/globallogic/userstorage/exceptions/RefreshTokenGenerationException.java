package com.globallogic.userstorage.exceptions;

public class RefreshTokenGenerationException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "The user token cannot be refreshed";

    public RefreshTokenGenerationException() {
        super(DEFAULT_MESSAGE);
    }

    public RefreshTokenGenerationException(String message) {
        super(message);
    }

    public RefreshTokenGenerationException(Throwable e) {
        super(e);
    }

    public RefreshTokenGenerationException(String message, Throwable e) {
        super(message, e);
    }

}
