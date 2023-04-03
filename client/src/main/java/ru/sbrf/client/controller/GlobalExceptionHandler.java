package ru.sbrf.client.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import ru.sbrf.common.messages.ErrorMessage;

import java.net.ConnectException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static String FAILED_CONNECT = "Failed to connect to the server";
    private final static String ATM_IS_NOT_AUTHORIZED = "The ATM is not authorized";
    private final static String OPERATION_INTERRUPTED = "The operation was interrupted";

    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    @ExceptionHandler
    public ErrorMessage ConnectExceptionHandler(ConnectException ex) {
        return new ErrorMessage(HttpStatus.GATEWAY_TIMEOUT.value(), FAILED_CONNECT);
    }

    @ExceptionHandler
    public ErrorMessage ForbiddenExceptionHandler(HttpClientErrorException.Forbidden ex) {
        return new ErrorMessage(ex.getStatusCode().value(), ATM_IS_NOT_AUTHORIZED);
    }

    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    @ExceptionHandler
    public ErrorMessage InterruptedExceptionHandler(InterruptedException ex) {
        return new ErrorMessage(HttpStatus.REQUEST_TIMEOUT.value(), OPERATION_INTERRUPTED);
    }
}