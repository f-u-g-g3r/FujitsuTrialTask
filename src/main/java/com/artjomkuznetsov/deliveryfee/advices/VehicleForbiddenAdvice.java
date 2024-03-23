package com.artjomkuznetsov.deliveryfee.advices;

import com.artjomkuznetsov.deliveryfee.controllers.responses.ErrorResponse;
import com.artjomkuznetsov.deliveryfee.exceptions.VehicleForbiddenException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class VehicleForbiddenAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(VehicleForbiddenException.class)
    protected ResponseEntity<Object> transportForbiddenHandler(RuntimeException exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
        return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }
}