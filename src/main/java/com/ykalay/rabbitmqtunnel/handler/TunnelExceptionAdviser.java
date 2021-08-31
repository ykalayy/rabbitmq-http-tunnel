package com.ykalay.rabbitmqtunnel.handler;

import com.ykalay.rabbitmqtunnel.http.TunnelHttpRequest;
import com.ykalay.rabbitmqtunnel.http.TunnelHttpResponse;
import com.ykalay.rabbitmqtunnel.rabbitmq.model.AmqpMessage;

/**
 * Rabbitmq HTTP->AMQP tunnel Exception adviser
 * Catch the possible exception and allows to the service handle them.
 *
 * @author ykalay
 *
 * @since 1.0
 */
public interface TunnelExceptionAdviser {

    TunnelHttpResponse handleException(TunnelHttpRequest httpRequest, Throwable throwable);
}
