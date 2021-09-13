package io.github.ykalay.rabbitmqtunnel.handler;

import io.github.ykalay.rabbitmqtunnel.http.TunnelHttpResponse;
import io.github.ykalay.rabbitmqtunnel.rabbitmq.model.AmqpMessage;

/**
 * Rabbitmq-http-tunnel timeout handler interface
 *
 * @author ykalay
 *
 * @since 1.0
 */
public interface HttpAmqpTunnelTimeoutHandler {

    TunnelHttpResponse handleTimeout(AmqpMessage requestMessage, String uri);
}
