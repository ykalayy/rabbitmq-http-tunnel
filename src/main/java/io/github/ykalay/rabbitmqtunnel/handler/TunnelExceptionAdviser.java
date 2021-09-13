package io.github.ykalay.rabbitmqtunnel.handler;

import io.github.ykalay.rabbitmqtunnel.http.TunnelHttpRequest;
import io.github.ykalay.rabbitmqtunnel.http.TunnelHttpResponse;

/**
 * Rabbitmq HTTP to AMQP tunnel Exception adviser
 * Catch the possible exception and allows to the service handle them.
 *
 * @author ykalay
 *
 * @since 1.0
 */
public interface TunnelExceptionAdviser {

    TunnelHttpResponse handleException(TunnelHttpRequest httpRequest, Throwable throwable);
}
