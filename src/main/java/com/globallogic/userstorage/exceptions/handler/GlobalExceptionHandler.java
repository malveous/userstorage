package com.globallogic.userstorage.exceptions.handler;

import com.globallogic.userstorage.exceptions.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException e) {
        log.debug("An error occurred while validating input fields", e);
        var errorMessage = e.getBindingResult().getAllErrors().stream()
                .map(error -> ((FieldError) error).getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(";"));
        return new ErrorResponse(errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse handleSQLIntegrityConstraintViolationException(DataIntegrityViolationException e) {
        log.debug("An error occurred while persisting data in data source", e);
        var rawExMsg = e.getMessage().toLowerCase();
        var errorMessage = "Data entered for the user is not valid for registration";
        if (rawExMsg.contains("email")) {
            errorMessage = "The email is already registered in the system";
        }
        return new ErrorResponse(errorMessage);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGenericExceptions(Exception e) {
        log.debug("An unexpected error occurred", e);
        return new ErrorResponse(e.getMessage());
    }

}
