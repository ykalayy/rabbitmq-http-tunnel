package com.ykalay.rabbitmqtunnel.core;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.ykalay.rabbitmqtunnel.config.NettyServerPreferences;
import com.ykalay.rabbitmqtunnel.config.RabbitmqPoolConfig;
import com.ykalay.rabbitmqtunnel.exception.MissingConfigurationException;
import com.ykalay.rabbitmqtunnel.handler.RestAmqpTunnelController;
import com.ykalay.rabbitmqtunnel.handler.RestAmqpTunnelTimeoutHandler;
import com.ykalay.rabbitmqtunnel.handler.TunnelExceptionAdviser;
import com.ykalay.rabbitmqtunnel.rabbitmq.connection.RabbitmqSingleConnectionPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Rabbitmq Tunnel Server Builder
 *
 * @author ykalay
 *
 * @since 1.0
 */
public class RabbitMqTunnelServerBuilder {

    /**
     * Rabbitmq-rest-tunnel Controller instances
     */
    private RestAmqpTunnelController[] restAmqpTunnelControllers;

    /**
     * Timeout Handler instance of tunnel server
     */
    private RestAmqpTunnelTimeoutHandler restAmqpTunnelTimeoutHandler;

    /**
     * Rabbitmq REST->AMQP tunnel Exception adviser instance
     */
    private TunnelExceptionAdviser tunnelExceptionAdviser;

    public RabbitMqTunnelServerBuilder() {}

    /**
     *
     * @param rabbitmqPoolConfig
     *          Apache Commons Pool2 {@link GenericObjectPoolConfig} instance for Rabbitmq Channels
     * @return Instance of this class
     */
    public RabbitMqTunnelServerBuilder setRabbitmqPoolConfig(GenericObjectPoolConfig<Channel> rabbitmqPoolConfig) {
        if(rabbitmqPoolConfig == null) {
            throw new IllegalArgumentException("RabbitmqPoolConfig cannot be null");
        }
        // Initialize default values... & And then set the user value
        RabbitmqPoolConfig.getInstance().setPoolConfig(rabbitmqPoolConfig);
        return this;
    }

    /**
     *
     *
     * @param rabbitmqConnection
     *
     * @return
     */
    public RabbitMqTunnelServerBuilder setRabbitmqConnection(Connection rabbitmqConnection) {
        if(rabbitmqConnection == null) {
            throw new IllegalArgumentException("RabbitmqConnection cannot be null");
        }
        // Initialize the SingleConnectionPool class and set the Connection value
        RabbitmqSingleConnectionPool.getInstance().setRabbitMqConnection(rabbitmqConnection);
        return this;
    }

    /**
     * Setter of restful amqp tunnel controllers. Which contains fucking with annotated with
     * @see com.ykalay.rabbitmqtunnel.annotation.RestAmqpTunnelRequestMapper
     *
     * @param restAmqpTunnelControllers
     *          Instance of Controller classes
     *
     * @return Instance of {@link RabbitMqTunnelServerBuilder}
     */
    public RabbitMqTunnelServerBuilder setRestAmqpTunnelControllers(RestAmqpTunnelController... restAmqpTunnelControllers) {
        if(restAmqpTunnelControllers == null) {
            throw new IllegalArgumentException("RestAmqpTunnelControllers cannot be null");
        }
        this.restAmqpTunnelControllers = restAmqpTunnelControllers;
        return this;
    }

    /**
     *
     * @param restAmqpTunnelTimeoutHandler
     *
     * @return Instance of {@link RabbitMqTunnelServerBuilder}
     */
    public RabbitMqTunnelServerBuilder setRestAmqpTunnelTimeoutHandler(RestAmqpTunnelTimeoutHandler restAmqpTunnelTimeoutHandler) {
        this.restAmqpTunnelTimeoutHandler = restAmqpTunnelTimeoutHandler;
        return this;
    }

    /**
     *
     * @param tunnelExceptionAdviser
     *
     * @return Instance of {@link RabbitMqTunnelServerBuilder}
     */
    public RabbitMqTunnelServerBuilder setTunnelExceptionAdviser(TunnelExceptionAdviser tunnelExceptionAdviser) {
        this.tunnelExceptionAdviser = tunnelExceptionAdviser;
        return this;
    }

    /**
     * Setter of server port
     *
     * @param port
     *          Port value of server
     *
     * @return Instance of {@link RabbitMqTunnelServerBuilder}
     */
    public RabbitMqTunnelServerBuilder setServerPort(int port) {
        if(port < 1) {
            throw new IllegalArgumentException("Port value is invalid");
        }
        NettyServerPreferences.getInstance().setNettyPort(port);
        return this;
    }

    /**
     * Setter of netty-server native support flag
     *
     * @param nativeSupport
     *      Netty native support enable or disabler flag
     *
     * @return Instance of {@link RabbitMqTunnelServerBuilder}
     */
    public RabbitMqTunnelServerBuilder setNettyNativeSupport(boolean nativeSupport) {
        NettyServerPreferences.getInstance().setNativeSupport(nativeSupport);
        return this;
    }

    /**
     * Buils the {@link RabbitmqTunnelServer} instance
     *
     * @return The powerful fresh RabbitMqTunnelServer instance
     *
     * @throws MissingConfigurationException When the validations are failed
     */
    public RabbitmqTunnelServer build() throws MissingConfigurationException {

        // Validation of Rabbitmq Connection
        if(RabbitmqSingleConnectionPool.getInstance().getRabbitMqConnection() == null) {
            throw new MissingConfigurationException("Rabbitmq connection cannot be null");
        }
        if(this.restAmqpTunnelControllers == null) {
            throw new MissingConfigurationException("Rabbitmq amqp tunnel controller list cannot be null");
        }
        // Initialize the server instance
        return new RabbitmqTunnelServer(this.restAmqpTunnelControllers,
                this.restAmqpTunnelTimeoutHandler, this.tunnelExceptionAdviser);
    }
}
