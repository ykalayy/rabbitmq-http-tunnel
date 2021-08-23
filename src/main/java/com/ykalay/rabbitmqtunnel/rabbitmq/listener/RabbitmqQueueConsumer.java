package com.ykalay.rabbitmqtunnel.rabbitmq.listener;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.ykalay.rabbitmqtunnel.core.netty.BaseNettyHandler;
import com.ykalay.rabbitmqtunnel.core.netty.NettyChannelStore;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.io.IOException;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;

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
            String statusCode = (String) properties.getHeaders().get("statusCode");
            // Get the channel from store
            nettyChannel = this.nettyChannelStore.getTunnelChannel(messageId).getNettyChannel();
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.valueOf(Integer.valueOf(statusCode)),
                    Unpooled.wrappedBuffer(body));
            response .headers().set(CONTENT_TYPE, "application/json");
            response .headers().set(CONTENT_LENGTH, response .content().readableBytes());
            nettyChannel.writeAndFlush(response);
        } catch (Exception e) {
            if(nettyChannel != null) {
                BaseNettyHandler.sendResponseWithNoBody(nettyChannel, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            }
        }

    }
}
