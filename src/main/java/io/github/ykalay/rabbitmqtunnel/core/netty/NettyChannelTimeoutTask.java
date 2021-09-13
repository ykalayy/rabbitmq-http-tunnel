package io.github.ykalay.rabbitmqtunnel.core.netty;

import io.github.ykalay.rabbitmqtunnel.rabbitmq.model.AmqpMessage;
import io.github.ykalay.rabbitmqtunnel.handler.HttpAmqpTunnelTimeoutHandler;
import io.github.ykalay.rabbitmqtunnel.http.TunnelHttpResponse;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpResponseStatus;

public class NettyChannelTimeoutTask implements Runnable {

    private final Channel channel;
    private final HttpAmqpTunnelTimeoutHandler httpAmqpTunnelTimeoutHandler;
    private final AmqpMessage amqpMessage;
    private final NettyChannelStore nettyChannelStore;

    public NettyChannelTimeoutTask(Channel channel, HttpAmqpTunnelTimeoutHandler httpAmqpTunnelTimeoutHandler, AmqpMessage<?> amqpMessage) {
        this.channel = channel;
        this.httpAmqpTunnelTimeoutHandler = httpAmqpTunnelTimeoutHandler;
        this.amqpMessage = amqpMessage;
        this.nettyChannelStore = NettyChannelStore.getInstance();
    }

    @Override
    public void run() {
        try {
            if(this.httpAmqpTunnelTimeoutHandler == null) {
                BaseNettyHandler.sendResponseWithNoBody(this.channel, HttpResponseStatus.REQUEST_TIMEOUT);
            } else {
                TunnelHttpResponse tunnelHttpResponse =
                        this.httpAmqpTunnelTimeoutHandler.handleTimeout(amqpMessage, BaseNettyHandler.getURIFromAttr(channel));
                BaseNettyHandler.sendJsonResponseWithBody(this.channel, tunnelHttpResponse);
            }
        } finally {
            this.nettyChannelStore.deRegisterNettyChannel(amqpMessage.getUniqueMessageId());
        }
    }
}
