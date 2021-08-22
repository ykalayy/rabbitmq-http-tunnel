package com.ykalay.rabbitmqtunnel.core.netty.model;

import com.ykalay.rabbitmqtunnel.rabbitmq.model.AmqpMessage;

import java.util.concurrent.ScheduledFuture;

public class TunnelChannel {

    private ScheduledFuture<?> timeoutHandler;
    private AmqpMessage<?> amqpMessage;

    public TunnelChannel(ScheduledFuture<?> timeoutHandler, AmqpMessage<?> amqpMessage) {
        this.timeoutHandler = timeoutHandler;
        this.amqpMessage = amqpMessage;
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
}
