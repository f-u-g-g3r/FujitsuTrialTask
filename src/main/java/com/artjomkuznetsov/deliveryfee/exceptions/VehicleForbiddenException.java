package com.artjomkuznetsov.deliveryfee.exceptions;

public class VehicleForbiddenException extends RuntimeException {
    public VehicleForbiddenException() {
        super("The use of this transport is forbidden due to weather conditions.");
    }
}
