package com.artjomkuznetsov.deliveryfee.controllers.exceptions.regionalBaseFeeNotFound;

import com.artjomkuznetsov.deliveryfee.controllers.exceptions.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RegionalBaseFeeNotFoundAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(RegionalBaseFeeNotFoundException.class)
    public ResponseEntity<Object> baseNotFoundHandler(RegionalBaseFeeNotFoundException exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
        return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
