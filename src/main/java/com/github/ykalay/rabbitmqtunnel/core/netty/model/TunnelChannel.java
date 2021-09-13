package com.github.ykalay.rabbitmqtunnel.core.netty.model;

import com.github.ykalay.rabbitmqtunnel.rabbitmq.model.AmqpMessage;
import io.netty.channel.Channel;

import java.util.concurrent.ScheduledFuture;

public class TunnelChannel {

    private ScheduledFuture<?> timeoutHandler;
    private AmqpMessage<?> amqpMessage;
    private Channel nettyChannel;

    public TunnelChannel(ScheduledFuture<?> timeoutHandler, AmqpMessage<?> amqpMessage, Channel nettyChannel) {
        this.timeoutHandler = timeoutHandler;
        this.amqpMessage = amqpMessage;
        this.nettyChannel = nettyChannel;
    }

    public ScheduledFuture<?> getTimeoutHandler() {
        return timeoutHandler;
    }

    public void setTimeoutHandler(ScheduledFuture<?> timeoutHandler) {
        this.timeoutHandler = timeoutHandler;
    }

    public AmqpMessage<?> getAmqpMessage() {
        return amqpMessage;
    }

    public void setAmqpMessage(AmqpMessage<?> amqpMessage) {
        this.amqpMessage = amqpMessage;
    }

    public Channel getNettyChannel() {
        return nettyChannel;
    }

    public void setNettyChannel(Channel nettyChannel) {
        this.nettyChannel = nettyChannel;
    }
}
