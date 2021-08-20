package com.ykalay.rabbitmqtunnel.rabbitmq.model;

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
     * All-args constructor of message
     *
     * @param uniqueMessageId
     *          Unique message identifier of message
     * @param messageBody
     *          Custom message body
     */
    public AmqpMessage(String uniqueMessageId, T messageBody) {
        this.uniqueMessageId = uniqueMessageId;
        this.messageBody = messageBody;
    }

    /**
     * @return value of message identifier as {@link Integer}
     */
    private String getUniqueMessageId() {
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
}
