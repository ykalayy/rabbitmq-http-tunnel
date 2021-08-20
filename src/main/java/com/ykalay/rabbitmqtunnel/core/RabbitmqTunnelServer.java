package com.ykalay.rabbitmqtunnel.core;

import com.ykalay.rabbitmqtunnel.handler.RestAmqpTunnelController;
import com.ykalay.rabbitmqtunnel.handler.RestAmqpTunnelTimeoutHandler;
import com.ykalay.rabbitmqtunnel.handler.TunnelExceptionAdviser;

/**
 *  Rabbitmq Tunnel Server
 *
 *      Initialize the netty, queues, exchanges, scans the RestMapper annotations and register them
 *
 * @author ykalay
 *
 * @since 1.0
 */
public class RabbitmqTunnelServer {

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

    public RabbitmqTunnelServer(RestAmqpTunnelController[] restAmqpTunnelControllers,
                                RestAmqpTunnelTimeoutHandler restAmqpTunnelTimeoutHandler,
                                TunnelExceptionAdviser tunnelExceptionAdviser) {
        this.restAmqpTunnelControllers = restAmqpTunnelControllers;
        this.restAmqpTunnelTimeoutHandler = restAmqpTunnelTimeoutHandler;
        this.tunnelExceptionAdviser = tunnelExceptionAdviser;
    }

    /**
     * Starts the Rabbitmq Tunnel Server
     */
    public void start() {

    }
}
