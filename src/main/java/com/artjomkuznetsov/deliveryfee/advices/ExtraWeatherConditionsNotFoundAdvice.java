package com.artjomkuznetsov.deliveryfee.advices;

import com.artjomkuznetsov.deliveryfee.controllers.responses.ErrorResponse;
import com.artjomkuznetsov.deliveryfee.exceptions.ExtraWeatherConditionsNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExtraWeatherConditionsNotFoundAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ExtraWeatherConditionsNotFoundException.class)
    public ResponseEntity<Object> weatherConditionsNotFoundHandler(ExtraWeatherConditionsNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
