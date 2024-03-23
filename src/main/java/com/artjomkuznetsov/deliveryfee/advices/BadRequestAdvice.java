package com.artjomkuznetsov.deliveryfee.advices;

import com.artjomkuznetsov.deliveryfee.controllers.responses.ErrorResponse;
import com.artjomkuznetsov.deliveryfee.exceptions.BadRequestBodyException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BadRequestAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> badRequestHandler(BadRequestException exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
        return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(BadRequestBodyException.class)
    protected ResponseEntity<Object> badRequestBodyHandler(BadRequestBodyException exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
        return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
