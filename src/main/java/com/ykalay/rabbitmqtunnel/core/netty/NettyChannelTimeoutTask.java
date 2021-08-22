package com.ykalay.rabbitmqtunnel.core.netty;

import com.ykalay.rabbitmqtunnel.handler.HttpAmqpTunnelTimeoutHandler;
import com.ykalay.rabbitmqtunnel.rabbitmq.model.AmqpMessage;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpResponseStatus;

public class NettyChannelTimeoutTask implements Runnable {

    private final Channel channel;
    private final HttpAmqpTunnelTimeoutHandler<?> httpAmqpTunnelTimeoutHandler;
    private final AmqpMessage amqpMessage;

    public NettyChannelTimeoutTask(Channel channel, HttpAmqpTunnelTimeoutHandler httpAmqpTunnelTimeoutHandler, AmqpMessage<?> amqpMessage) {
        this.channel = channel;
        this.httpAmqpTunnelTimeoutHandler = httpAmqpTunnelTimeoutHandler;
        this.amqpMessage = amqpMessage;
    }

    @Override
    public void run() {
        if(httpAmqpTunnelTimeoutHandler == null) {
            BaseNettyHandler.sendResponseWithNoBody(channel, HttpResponseStatus.REQUEST_TIMEOUT);
        } else {
            httpAmqpTunnelTimeoutHandler.handleTimeout(amqpMessage, BaseNettyHandler.getURIFromAttr(channel));
        }
    }
}
