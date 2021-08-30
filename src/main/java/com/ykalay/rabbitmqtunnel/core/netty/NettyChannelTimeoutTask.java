package com.ykalay.rabbitmqtunnel.core.netty;

import com.ykalay.rabbitmqtunnel.handler.HttpAmqpTunnelTimeoutHandler;
import com.ykalay.rabbitmqtunnel.rabbitmq.model.AmqpMessage;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpResponseStatus;

public class NettyChannelTimeoutTask implements Runnable {

    private final Channel channel;
    private final HttpAmqpTunnelTimeoutHandler<?> httpAmqpTunnelTimeoutHandler;
    private final AmqpMessage amqpMessage;
    private final NettyChannelStore nettyChannelStore;

    public NettyChannelTimeoutTask(Channel channel, HttpAmqpTunnelTimeoutHandler<?> httpAmqpTunnelTimeoutHandler, AmqpMessage<?> amqpMessage) {
        this.channel = channel;
        this.httpAmqpTunnelTimeoutHandler = httpAmqpTunnelTimeoutHandler;
        this.amqpMessage = amqpMessage;
        this.nettyChannelStore = NettyChannelStore.getInstance();
    }

    @Override
    public void run() {
        try {
            if(this.httpAmqpTunnelTimeoutHandler == null) {
                BaseNettyHandler.sendResponseWithNoBody(channel, HttpResponseStatus.REQUEST_TIMEOUT);
            } else {
                this.httpAmqpTunnelTimeoutHandler.handleTimeout(amqpMessage, BaseNettyHandler.getURIFromAttr(channel));
            }
        } finally {
            this.nettyChannelStore.deRegisterNettyChannel(amqpMessage.getUniqueMessageId());
        }
    }
}
