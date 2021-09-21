package io.github.ykalay.rabbitmqtunnel.rabbitmq.listener;

import com.rabbitmq.client.*;
import io.github.ykalay.rabbitmqtunnel.core.netty.BaseNettyHandler;
import io.github.ykalay.rabbitmqtunnel.core.netty.NettyChannelStore;
import io.github.ykalay.rabbitmqtunnel.handler.TunnelResponseInterceptor;
import io.github.ykalay.rabbitmqtunnel.http.TunnelHttpResponse;
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
    private final TunnelResponseInterceptor responseInterceptor;

    public RabbitmqQueueConsumer(NettyChannelStore nettyChannelStore, TunnelResponseInterceptor responseInterceptor) {
        this.nettyChannelStore = nettyChannelStore;
        this.responseInterceptor = responseInterceptor;
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
            // Get the channel from store
            nettyChannel = this.nettyChannelStore.getTunnelChannel(messageId).getNettyChannel();
            TunnelHttpResponse tunnelHttpResponse = this.responseInterceptor.handleResponse(properties, body);
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
