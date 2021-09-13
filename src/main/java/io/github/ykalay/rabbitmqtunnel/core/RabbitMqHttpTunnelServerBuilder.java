package io.github.ykalay.rabbitmqtunnel.core;

import io.github.ykalay.rabbitmqtunnel.annotation.AmqpTunnelRequestMapper;
import io.github.ykalay.rabbitmqtunnel.config.NettyServerPreferences;
import io.github.ykalay.rabbitmqtunnel.config.RabbitmqPoolConfig;
import io.github.ykalay.rabbitmqtunnel.config.RabbitmqServerConfig;
import io.github.ykalay.rabbitmqtunnel.exception.MissingConfigurationException;
import io.github.ykalay.rabbitmqtunnel.rabbitmq.connection.RabbitmqSingleConnectionPool;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import io.github.ykalay.rabbitmqtunnel.handler.HttpAmqpTunnelController;
import io.github.ykalay.rabbitmqtunnel.handler.HttpAmqpTunnelTimeoutHandler;
import io.github.ykalay.rabbitmqtunnel.handler.TunnelExceptionAdviser;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Rabbitmq Tunnel Server Builder
 *
 * @author ykalay
 *
 * @since 1.0
 */
public class RabbitMqHttpTunnelServerBuilder {

    /**
     * Rabbitmq-Http-tunnel Controller instances
     */
    private HttpAmqpTunnelController[] httpAmqpTunnelControllers;

    /**
     * Timeout Handler instance of tunnel server
     */
    private HttpAmqpTunnelTimeoutHandler httpAmqpTunnelTimeoutHandler;

    /**
     * Rabbitmq HTTP->AMQP tunnel Exception adviser instance
     */
    private TunnelExceptionAdviser tunnelExceptionAdviser;

    public RabbitMqHttpTunnelServerBuilder() {}

    /**
     * Setter of rabbitmq pool config
     *
     * @param rabbitmqPoolConfig
     *          Apache Commons Pool2 {@link GenericObjectPoolConfig} instance for Rabbitmq Channels
     *
     * @return Instance of this class
     */
    public RabbitMqHttpTunnelServerBuilder setRabbitmqPoolConfig(GenericObjectPoolConfig<Channel> rabbitmqPoolConfig) {
        if(rabbitmqPoolConfig == null) {
            throw new IllegalArgumentException("RabbitmqPoolConfig cannot be null");
        }
        // Initialize default values... & And then set the user value
        RabbitmqPoolConfig.getInstance().setPoolConfig(rabbitmqPoolConfig);
        return this;
    }

    /**
     * Setter of rabbitmq connection instance
     *
     * @param rabbitmqConnection
     *          Rabbitmq connection instance
     *
     * @return Instance of {@link RabbitMqHttpTunnelServerBuilder}
     */
    public RabbitMqHttpTunnelServerBuilder setRabbitmqConnection(Connection rabbitmqConnection) {
        if(rabbitmqConnection == null) {
            throw new IllegalArgumentException("RabbitmqConnection cannot be null");
        }
        // Initialize the SingleConnectionPool class and set the Connection value
        RabbitmqSingleConnectionPool.getInstance().setRabbitMqConnection(rabbitmqConnection);
        return this;
    }

    /**
     * Setter of HTTP to AMQP tunnel controllers. Which should contain functions with annotated with
     * @see AmqpTunnelRequestMapper
     *
     * @param httpAmqpTunnelControllers
     *          Instance of Controller classes
     *
     * @return Instance of {@link RabbitMqHttpTunnelServerBuilder}
     */
    public RabbitMqHttpTunnelServerBuilder setHttpAmqpTunnelControllers(HttpAmqpTunnelController... httpAmqpTunnelControllers) {
        if(httpAmqpTunnelControllers == null) {
            throw new IllegalArgumentException("HttpAmqpTunnelControllers cannot be null");
        }
        this.httpAmqpTunnelControllers = httpAmqpTunnelControllers;
        return this;
    }

    /**
     * Setter of TimeoutHandler of tunnel server
     *
     * @param httpAmqpTunnelTimeoutHandler
     *          A Timeout handler class which implements the {@link HttpAmqpTunnelTimeoutHandler} interface
     *
     * @return Instance of {@link RabbitMqHttpTunnelServerBuilder}
     */
    public RabbitMqHttpTunnelServerBuilder setHttpAmqpTunnelTimeoutHandler(HttpAmqpTunnelTimeoutHandler httpAmqpTunnelTimeoutHandler) {
        this.httpAmqpTunnelTimeoutHandler = httpAmqpTunnelTimeoutHandler;
        return this;
    }

    /**
     * Setter of Tunnel exception handler
     *
     * @param tunnelExceptionAdviser
     *           A exception handler class which implements the {@link TunnelExceptionAdviser}
     *
     * @return Instance of {@link RabbitMqHttpTunnelServerBuilder}
     */
    public RabbitMqHttpTunnelServerBuilder setTunnelExceptionAdviser(TunnelExceptionAdviser tunnelExceptionAdviser) {
        this.tunnelExceptionAdviser = tunnelExceptionAdviser;
        return this;
    }

    /**
     * Setter of server port
     *
     * @param port
     *          Port value of server
     *
     * @return Instance of {@link RabbitMqHttpTunnelServerBuilder}
     */
    public RabbitMqHttpTunnelServerBuilder setServerPort(int port) {
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
     * @return Instance of {@link RabbitMqHttpTunnelServerBuilder}
     */
    public RabbitMqHttpTunnelServerBuilder setNettyNativeSupport(boolean nativeSupport) {
        NettyServerPreferences.getInstance().setNativeSupport(nativeSupport);
        return this;
    }

    public RabbitMqHttpTunnelServerBuilder setTimeoutSec(int timeoutSec) {
        if(timeoutSec < 1) {
            throw new IllegalArgumentException("timeout should be more than 1 sec");
        }
        NettyServerPreferences.getInstance().setNettyTimeoutSec(timeoutSec);
        return this;
    }

    /**
     * Setter of serviceName, It sets the rabbitmqQueue and rabbitmqExchange name of server
     *
     * This Queue and Exchange component names can be set by Environment variables
     *
     * @param serviceName
     *          Service Name
     *
     * @return Instance of {@link RabbitMqHttpTunnelServerBuilder}
     */
    public RabbitMqHttpTunnelServerBuilder setServiceName(String serviceName) {
        if(serviceName == null) {
            throw new IllegalArgumentException("Service name cannot be null");
        }
        // Setting queue & exchange name with postfix of component name
        RabbitmqServerConfig.getInstance().setExchangeName(serviceName.concat("-responseExchange"));
        RabbitmqServerConfig.getInstance().setQueueName(serviceName.concat("-responseQueue"));
        return this;
    }

    /**
     * Buils the {@link RabbitmqHttpTunnelServer} instance
     *
     * @return The powerful fresh RabbitMqTunnelServer instance
     *
     * @throws MissingConfigurationException When the validations are failed
     */
    public RabbitmqHttpTunnelServer build() throws MissingConfigurationException {

        // Validation of Rabbitmq Connection
        if(RabbitmqSingleConnectionPool.getInstance().getRabbitMqConnection() == null) {
            throw new MissingConfigurationException("Rabbitmq connection cannot be null");
        }
        if(this.httpAmqpTunnelControllers == null) {
            throw new MissingConfigurationException("Rabbitmq amqp tunnel controller list cannot be null");
        }
        // Initialize the server instance
        return new RabbitmqHttpTunnelServer(this.httpAmqpTunnelControllers,
                this.httpAmqpTunnelTimeoutHandler, this.tunnelExceptionAdviser);
    }
}
