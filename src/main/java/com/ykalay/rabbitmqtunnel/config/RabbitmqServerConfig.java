package com.ykalay.rabbitmqtunnel.config;

import java.util.Objects;

/**
 * Rabbitmq Server configuration class
 *
 * @implSpec  System Environments:
 *          RABBITMQ_TUNNEL_HOST: Host of rabbitmq to connect
 *          RABBITMQ_TUNNEL_PORT: Port of rabbitmq to connect
 *          RABBITMQ_TUNNEL_EXCHANGE_NAME: Name of exchange to create
 *          RABBITMQ_TUNNEL_QUEUE_NAME: Name of queue to bind exchange & is listened by tunnel library for responses
 *
 * @author ykalay
 *
 * @since 1.0
 */
public class RabbitmqServerConfig {

    /**
     * Singleton lazy instance of {@link RabbitmqServerConfig}
     */
    private static RabbitmqServerConfig LAZY_HOLDER;

    /**
     * @return singleton lazy instance of {@link RabbitmqServerConfig}
     */
    public static RabbitmqServerConfig getInstance() {
        if(Objects.isNull(LAZY_HOLDER)) {
            LAZY_HOLDER = new RabbitmqServerConfig();
        }
        return  LAZY_HOLDER;
    }

    private RabbitmqServerConfig() {init();}

    private static final String DEFAULT_RABBITMQ_HOST = "localhost";

    private static final int DEFAULT_RABBITMQ_PORT = 5672;

    private static final String DEFAULT_EXCHANGE_NAME = "rabbitmq-http-tunnel-responseExchange";

    private static final String DEFAULT_QUEUE_NAME = "rabbitmq-http-tunnel-responseQueue";

    private static final int DEFAULT_SELF_QUEUE_CONSUMER_COUNT = Runtime.getRuntime().availableProcessors() * 2;

    public static final String AMQP_RESPONSE_STATUS_CODE = "statusCode";

    private int rabbitmqPort = DEFAULT_RABBITMQ_PORT;

    private String rabbitmqHost = DEFAULT_RABBITMQ_HOST;

    private String exchangeName = DEFAULT_EXCHANGE_NAME;

    private String queueName = DEFAULT_QUEUE_NAME;

    private int selfQueueConsumerCount = DEFAULT_SELF_QUEUE_CONSUMER_COUNT;

    public int getRabbitmqPort() {
        return rabbitmqPort;
    }

    public void setRabbitmqPort(int rabbitmqPort) {
        this.rabbitmqPort = rabbitmqPort;
    }

    public String getRabbitmqHost() {
        return rabbitmqHost;
    }

    public void setRabbitmqHost(String rabbitmqHost) {
        this.rabbitmqHost = rabbitmqHost;
    }

    public int getSelfQueueConsumerCount() {
        return selfQueueConsumerCount;
    }

    public void setSelfQueueConsumerCount(int selfQueueConsumerCount) {
        this.selfQueueConsumerCount = selfQueueConsumerCount;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    private void init() {

    }
}
