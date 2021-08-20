package com.ykalay.rabbitmqtunnel.exception;

public class MissingConfigurationException extends Exception{

    private final String message;

    public MissingConfigurationException(String message) {
        super(message);
        this.message = message;
    }
}
