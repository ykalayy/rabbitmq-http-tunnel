package com.github.ykalay.rabbitmqtunnel.support;

import java.util.UUID;

public class IdGenerator {

    private IdGenerator() {}

    /**
     * Generates random UID for each-message
     */
    public static String generateUniqueMessageId() {
        return UUID.randomUUID().toString();
    }
}
