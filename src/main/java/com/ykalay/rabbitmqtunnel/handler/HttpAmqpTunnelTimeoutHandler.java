package com.ykalay.rabbitmqtunnel.handler;

import com.ykalay.rabbitmqtunnel.rabbitmq.model.AmqpMessage;

/**
 * Rabbitmq-http-tunnel timeout handler interface
 *
 * @author ykalay
 *
 * @since 1.0
 */
public interface HttpAmqpTunnelTimeoutHandler<T> {


    byte[] handleTimeout(AmqpMessage<T> requestMessage, String uri);
}
