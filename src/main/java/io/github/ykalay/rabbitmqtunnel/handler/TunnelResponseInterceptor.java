package io.github.ykalay.rabbitmqtunnel.handler;

import com.rabbitmq.client.AMQP;
import io.github.ykalay.rabbitmqtunnel.config.RabbitmqServerConfig;
import io.github.ykalay.rabbitmqtunnel.http.TunnelHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import static io.github.ykalay.rabbitmqtunnel.support.StatusCodeHelper.getStatusCode;

public interface TunnelResponseInterceptor {

    TunnelHttpResponse handleResponse(AMQP.BasicProperties properties, byte[] body);

    TunnelResponseInterceptor DEFAULT = (properties, body) -> {
        int statusCode = getStatusCode(properties.getHeaders().get(RabbitmqServerConfig.AMQP_RESPONSE_STATUS_CODE));
        TunnelHttpResponse tunnelHttpResponse = new TunnelHttpResponse();
        tunnelHttpResponse.setBody(body);
        tunnelHttpResponse.setHttpResponseStatus(HttpResponseStatus.valueOf(statusCode));
        return tunnelHttpResponse;
    };
}
