package com.smartcampus;

public class SensorUnavailableException extends RuntimeException {
    public SensorUnavailableException(String sensorId) {
        super("Sensor '" + sensorId + "' is under maintenance and cannot accept readings.");
    }
}