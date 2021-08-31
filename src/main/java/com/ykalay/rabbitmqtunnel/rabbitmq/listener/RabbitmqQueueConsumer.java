package com.ykalay.rabbitmqtunnel.rabbitmq.listener;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.ykalay.rabbitmqtunnel.config.RabbitmqServerConfig;
import com.ykalay.rabbitmqtunnel.core.netty.BaseNettyHandler;
import com.ykalay.rabbitmqtunnel.core.netty.NettyChannelStore;
import com.ykalay.rabbitmqtunnel.http.TunnelHttpResponse;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.IOException;

/**
 *
 *
 * @author ykalay
 *
 * @since 1.0
 */
public class RabbitmqQueueConsumer implements Consumer {

    private final NettyChannelStore nettyChannelStore;

    public RabbitmqQueueConsumer(NettyChannelStore nettyChannelStore) {
        this.nettyChannelStore = nettyChannelStore;
    }

    @Override
    public void handleConsumeOk(String consumerTag) {

    }

    @Override
    public void handleCancelOk(String consumerTag) {

    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {

    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {

    }

    @Override
    public void handleRecoverOk(String consumerTag) {

    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

        Channel nettyChannel = null;
        try {
            String messageId = properties.getMessageId();
            Long statusCode = (Long) properties.getHeaders().get(RabbitmqServerConfig.AMQP_RESPONSE_STATUS_CODE);
            // Get the channel from store
            nettyChannel = this.nettyChannelStore.getTunnelChannel(messageId).getNettyChannel();
            TunnelHttpResponse tunnelHttpResponse = new TunnelHttpResponse();
            tunnelHttpResponse.setBody(body);
            tunnelHttpResponse.setHttpResponseStatus(HttpResponseStatus.valueOf(Math.toIntExact(statusCode)));
            BaseNettyHandler.sendJsonResponseWithBody(nettyChannel, tunnelHttpResponse);
            // De-register the channel & remove timeout task
            this.nettyChannelStore.deRegisterNettyChannel(messageId);
        } catch (Exception e) {
            e.printStackTrace();
            if(nettyChannel != null) {
                BaseNettyHandler.sendResponseWithNoBody(nettyChannel, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            }
        }

    }
}
