package com.artjomkuznetsov.deliveryfee.exceptions;

public class RegionalBaseFeeNotFoundException extends RuntimeException {
    public RegionalBaseFeeNotFoundException(String city) {
        super("Regional base fee for city " + city + " is not found.");
    }
}
