package com.github.ykalay.rabbitmqtunnel.rabbitmq.model;

import com.github.ykalay.rabbitmqtunnel.annotation.AmqpTunnelRequestMapper;

/**
 * Message of tunnel
 *
 * @author ykalay
 *
 * @since 1.0
 */
public class AmqpMessage<T> extends BaseMessage {

    /**
     * Unique message-Id, It sets by library, Shouldn't be touch in service level
     */
    private String uniqueMessageId;

    /**
     * Target Exchange, By default,
     * It is set by
     *          {@link AmqpTunnelRequestMapper}
     *                      If targetExchange value is sett
     */
    private String targetExchange;

    /**
     * Routing key of the message
     */
    private String routingKey;

    /**
     * Custom message body
     */
    private T messageBody;

    /**
     * No-args constructor of message
     */
    public AmqpMessage() {}

    /**
     * Constructor of message
     *
     * @param messageBody
     *          Custom message body
     * @param routingKey
     *          Routing-key
     */
    public AmqpMessage(String routingKey, T messageBody) {
        this.routingKey = routingKey;
        this.messageBody = messageBody;
    }

    /**
     * All-args Constructor of message
     *
     * @param routingKey
     *          Routing-key
     * @param  targetExchange
     *          Target exchange name
     * @param messageBody
     *          Custom message body
     */
    public AmqpMessage(String routingKey, String targetExchange, T messageBody) {
        this.routingKey = routingKey;
        this.targetExchange = targetExchange;
        this.messageBody = messageBody;
    }

    /**
     * @return value of message identifier as {@link Integer}
     */
    public String getUniqueMessageId() {
        return uniqueMessageId;
    }

    /**
     * Setter of unique message identifier
     *
     * @param uniqueMessageId
     *          New message identifier. WARNING ! ! ! Do not set it from service code
     */
    private void setUniqueMessageId(String uniqueMessageId) {
        this.uniqueMessageId = uniqueMessageId;
    }

    /**
     * @return value of generic message body
     */
    public T getMessageBody() {
        return messageBody;
    }

    /**
     * Setter of generic message body
     *
     * @param messageBody
     *          Custom message body
     */
    public void setMessageBody(T messageBody) {
        this.messageBody = messageBody;
    }

    /**
     * @return value of message routing-key
     */
    public String getRoutingKey() {
        return routingKey;
    }

    /**
     * Setter of message routing key
     *
     * @param routingKey
     *          Routing key for the message
     */
    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    /**
     * @return value of message target-exchange-name
     */
    public String getTargetExchange() {
        return targetExchange;
    }

    /**
     * Setter of targetExchange of message
     *
     * @param targetExchange
     *          Target exchange name for incoming-http request
     */
    public void setTargetExchange(String targetExchange) {
        this.targetExchange = targetExchange;
    }
}
