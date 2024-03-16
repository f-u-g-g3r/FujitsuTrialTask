package com.artjomkuznetsov.deliveryfee.controllers.exceptions.regionalBaseFeeNotFound;

public class RegionalBaseFeeNotFoundException extends RuntimeException {
    public RegionalBaseFeeNotFoundException(String city) {
        super("Regional base fee for city " + city + " is not found.");
    }

    public RegionalBaseFeeNotFoundException(Long id) {
        super("Regional base fee with id " + id + "is not found.");
    }
}
