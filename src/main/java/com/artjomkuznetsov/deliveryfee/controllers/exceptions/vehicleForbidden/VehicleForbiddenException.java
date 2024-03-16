package com.artjomkuznetsov.deliveryfee.controllers.exceptions.vehicleForbidden;

public class VehicleForbiddenException extends RuntimeException {
    public VehicleForbiddenException() {
        super("The use of this transport is forbidden due to weather conditions.");
    }
}
