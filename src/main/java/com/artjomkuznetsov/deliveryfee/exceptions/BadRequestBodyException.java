package com.artjomkuznetsov.deliveryfee.exceptions;

public class BadRequestBodyException extends RuntimeException {
    public BadRequestBodyException(String message) {
        super(message);
    }
}
