package com.github.ykalay.rabbitmqtunnel.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.github.ykalay.rabbitmqtunnel.config.RabbitmqServerConfig;
import com.github.ykalay.rabbitmqtunnel.rabbitmq.pool.RabbitmqChannelStore;

import java.util.HashMap;
import java.util.Objects;

/**
 * Rabbitmq Env Initializer - Exchange-Queue-Listeners
 *
 * @author ykalay
 *
 * @since 1.0
 *
 */
public class RabbitmqEnvironmentInitializer {

    /**
     * Singleton lazy instance of {@link RabbitmqEnvironmentInitializer}
     */
    private static RabbitmqEnvironmentInitializer LAZY_HOLDER;

    /**
     * @return singleton lazy instance of {@link RabbitmqEnvironmentInitializer}
     */
    public static RabbitmqEnvironmentInitializer getInstance() {
        if(Objects.isNull(LAZY_HOLDER)) {
            LAZY_HOLDER = new RabbitmqEnvironmentInitializer();
        }
        return LAZY_HOLDER;
    }

    private final RabbitmqChannelStore rabbitmqChannelStore;
    private final RabbitmqServerConfig rabbitmqServerConfig;
    private static final String ANY_ROUTING_KEY = "anyRoutingKey";

    private RabbitmqEnvironmentInitializer() {
        this.rabbitmqChannelStore = RabbitmqChannelStore.getInstance();
        this.rabbitmqServerConfig = RabbitmqServerConfig.getInstance();
    }

    /**
     * The tunnel server fan-out exchange initializer
     *
     * @throws Exception When the exception happens while rabbitmq operations or channel pool operations
     */
    public void initExchange() throws Exception {

        // Get a channel from store
        Channel channel = this.rabbitmqChannelStore.getChannel();
        channel.exchangeDeclare(this.rabbitmqServerConfig.getExchangeName(), BuiltinExchangeType.FANOUT);

        // Return channel back to pool
        this.rabbitmqChannelStore.returnChannel(channel);
    }

    /**
     * The tunnel server response queue initializer that's bound to the fan-out tunnel server exchange
     *
     * @throws Exception When the exception happens while rabbitmq operations or channel pool operations
     */
    public void initResponseQueue() throws Exception {

        Channel channel = null;
        channel = this.rabbitmqChannelStore.getChannel();
        // What about giving option to configure the arguments ???
        // https://github.com/ykalay/rabbitmq-http-tunnel/issues/3
        channel.queueDeclare(this.rabbitmqServerConfig.getQueueName(), false,
                false, true, new HashMap<>());
        // Bind the queue with the exchange
        channel.queueBind(this.rabbitmqServerConfig.getQueueName(), this.rabbitmqServerConfig.getExchangeName(),
                ANY_ROUTING_KEY);

        // Return channel back to pool
        this.rabbitmqChannelStore.returnChannel(channel);
    }

    public void initListener() {

    }
}
