package io.github.ykalay.rabbitmqtunnel.support;

import java.util.UUID;

public class IdGenerator {

    private IdGenerator() {}

    /**
     * Generates random UID for each-message
     *
     * @return the unique UID as String
     */
    public static String generateUniqueMessageId() {
        return UUID.randomUUID().toString();
    }
}
