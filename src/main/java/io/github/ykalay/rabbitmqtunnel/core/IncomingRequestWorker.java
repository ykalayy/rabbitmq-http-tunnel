package io.github.ykalay.rabbitmqtunnel.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ykalay.rabbitmqtunnel.http.TunnelHttpRequest;
import io.github.ykalay.rabbitmqtunnel.rabbitmq.model.AmqpMessage;
import io.github.ykalay.rabbitmqtunnel.rabbitmq.pool.RabbitmqChannelStore;
import io.github.ykalay.rabbitmqtunnel.support.IdGenerator;
import com.rabbitmq.client.AMQP;
import io.github.ykalay.rabbitmqtunnel.annotation.AmqpTunnelRequestMapper;
import io.github.ykalay.rabbitmqtunnel.core.netty.BaseNettyHandler;
import io.github.ykalay.rabbitmqtunnel.core.netty.NettyChannelStore;
import io.github.ykalay.rabbitmqtunnel.handler.TunnelExceptionAdviser;
import io.github.ykalay.rabbitmqtunnel.http.TunnelHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang3.reflect.FieldUtils;

public class IncomingRequestWorker implements Runnable {

    private final TunnelHttpRequest tunnelHttpRequest;

    private final HttpAmqpControllerModel httpAmqpControllerModel;

    private final RabbitmqChannelStore rabbitmqChannelStore;

    private final NettyChannelStore nettyChannelStore;

    private final io.netty.channel.Channel nettyChannel;

    private final HttpRequest httpRequest;

    private final ObjectMapper objectMapper;

    private TunnelExceptionAdviser tunnelExceptionAdviser;

    public IncomingRequestWorker(TunnelHttpRequest tunnelHttpRequest, HttpAmqpControllerModel httpAmqpControllerModel,
                                 io.netty.channel.Channel nettyChannel,
                                 HttpRequest httpRequest) {
        this.tunnelHttpRequest = tunnelHttpRequest;
        this.httpAmqpControllerModel = httpAmqpControllerModel;
        this.rabbitmqChannelStore = RabbitmqChannelStore.getInstance();
        this.nettyChannelStore = NettyChannelStore.getInstance();
        this.nettyChannel = nettyChannel;
        this.httpRequest = httpRequest;
        this.objectMapper = new ObjectMapper();
        this.tunnelExceptionAdviser = HttpAmqpControllerModelStore.getInstance().getTimeoutController();
    }

    @Override
    public void run() {
        com.rabbitmq.client.Channel channel = null;
        try {
            AmqpMessage amqpMessage = (AmqpMessage) httpAmqpControllerModel.getControllerMethod()
                    .invoke(httpAmqpControllerModel.getControllerInstance(), tunnelHttpRequest);
            channel = this.rabbitmqChannelStore.getChannel();
            // Set the uniqueId to the amqp message
            setUniqueMessageId(amqpMessage);

            String targetExchange = findTargetExchange(amqpMessage, this.httpAmqpControllerModel.getAmqpTunnelRequestMapper());

            // Set timeout task
            this.nettyChannelStore.registerNettyChannel(amqpMessage, this.nettyChannel);

            AMQP.BasicProperties basicProperties = generateBasicPropWithMessageId(amqpMessage.getUniqueMessageId());

            // Send message to the targetExchange...
            channel.basicPublish(targetExchange, amqpMessage.getRoutingKey(), basicProperties, this.objectMapper.writeValueAsBytes(amqpMessage.getMessageBody()));
        } catch (Exception e) {
            if(this.tunnelExceptionAdviser == null) {
                e.printStackTrace();
            } else {
                TunnelHttpResponse tunnelHttpResponse = this.tunnelExceptionAdviser.handleException(this.tunnelHttpRequest, e.getCause());
                BaseNettyHandler.sendJsonResponseWithBody(this.nettyChannel, tunnelHttpResponse);
            }
        } finally {
            if(channel != null) {
                try {
                    this.rabbitmqChannelStore.returnChannel(channel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ReferenceCountUtil.release(httpRequest);
        }
    }

    private AMQP.BasicProperties generateBasicPropWithMessageId(String uniqueMessageId) {
        return new AMQP.BasicProperties(null, null, null,
                null, null, null, null, null, uniqueMessageId,
                null, null, null, null, null);
    }

    private String findTargetExchange(AmqpMessage amqpMessage, AmqpTunnelRequestMapper amqpTunnelRequestMapper) {
        String targetExchange;
        if(!amqpTunnelRequestMapper.targetExchange().equals("undefined")) {
            targetExchange = amqpTunnelRequestMapper.targetExchange();
        } else {
            targetExchange = amqpMessage.getTargetExchange();
        }
        return targetExchange;
    }

    private void setUniqueMessageId(AmqpMessage amqpMessage) throws IllegalAccessException, NoSuchFieldException {
        FieldUtils.writeField(amqpMessage, "uniqueMessageId", IdGenerator.generateUniqueMessageId(), true);
    }
}
