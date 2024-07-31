package com.globallogic.userstorage.exceptions.handler;

import com.globallogic.userstorage.exceptions.MissingUserTokenException;
import com.globallogic.userstorage.exceptions.RefreshTokenGenerationException;
import com.globallogic.userstorage.exceptions.UnauthorizedUserException;
import com.globallogic.userstorage.exceptions.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        log.debug("An error occurred while validating input fields", e);
        return e.getBindingResult().getAllErrors().stream()
                .map(error -> new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                        ((FieldError) error).getField() + ": " + error.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public List<ErrorResponse> handleSQLIntegrityConstraintViolationException(DataIntegrityViolationException e) {
        log.debug("An error occurred while persisting data in data source", e);
        var rawExMsg = e.getMessage();
        String errorMessage = null;

        if (Objects.isNull(rawExMsg)) {
            errorMessage = "Data entered for the user is not valid for registration";
        } else {
            rawExMsg = rawExMsg.toLowerCase();
            if (rawExMsg.contains("email")) {
                errorMessage = "The email is already registered in the system";
            }
        }

        return List.of(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingUserTokenException.class)
    public List<ErrorResponse> handleMissingUserTokenException(MissingUserTokenException e) {
        log.warn("The user token is not present in the request");
        return List.of(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedUserException.class)
    public List<ErrorResponse> handleMissingUserTokenException(UnauthorizedUserException e) {
        log.warn("The data provided for user authentication is not valid");
        return List.of(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RefreshTokenGenerationException.class)
    public List<ErrorResponse> handleRefreshTokenGenerationException(RefreshTokenGenerationException e) {
        log.warn("The system was not able to update the token and login details");
        return List.of(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public List<ErrorResponse> handleGenericExceptions(Exception e) {
        log.debug("An unexpected error occurred", e);
        return List.of(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

}
