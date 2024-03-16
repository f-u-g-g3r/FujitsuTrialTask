package com.artjomkuznetsov.deliveryfee.exceptions;

public class ExtraWeatherConditionsNotFoundException extends RuntimeException {
    public ExtraWeatherConditionsNotFoundException() {
        super("Extra fees for weather conditions haven't been configured. The database may not be configured correctly.");
    }
}
