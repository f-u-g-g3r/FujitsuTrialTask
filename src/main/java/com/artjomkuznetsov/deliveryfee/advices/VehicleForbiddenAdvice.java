package com.artjomkuznetsov.deliveryfee.advices;

import com.artjomkuznetsov.deliveryfee.controllers.responses.ErrorResponse;
import com.artjomkuznetsov.deliveryfee.exceptions.VehicleForbiddenException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class VehicleForbiddenAdvice extends ResponseEntityExceptionHandler {
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(VehicleForbiddenException.class)
    @ApiResponse(responseCode = "403", description = "", content = @Content)
    @ResponseBody
    protected ResponseEntity<Object> transportForbiddenHandler(RuntimeException exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
        return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }
}