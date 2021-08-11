package com.ykalay.rabbitmqtunnel.rabbitmq.connection;

import java.util.Objects;

/**
 * Rabbitmq single connection manager class
 *
 * @author ykalay
 *
 * @since 1.0
 */
public class RabbitmqSingleConnectionPool {

    /**
     * Singleton lazy instance of {@link RabbitmqSingleConnectionPool}
     */
    private static RabbitmqSingleConnectionPool LAZY_HOLDER;

    /**
     * @return singleton lazy instance of {@link RabbitmqSingleConnectionPool}
     */
    public static RabbitmqSingleConnectionPool getInstance() {
        if(Objects.isNull(LAZY_HOLDER)) {
            LAZY_HOLDER = new RabbitmqSingleConnectionPool();
        }
        return LAZY_HOLDER;
    }

    private RabbitmqSingleConnectionPool() {

    }

}
