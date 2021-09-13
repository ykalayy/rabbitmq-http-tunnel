package com.github.ykalay.rabbitmqtunnel.core.netty;

import com.github.ykalay.rabbitmqtunnel.core.netty.model.TunnelChannel;
import com.github.ykalay.rabbitmqtunnel.rabbitmq.model.AmqpMessage;
import com.github.ykalay.rabbitmqtunnel.handler.HttpAmqpTunnelTimeoutHandler;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * Netty Channel Store
 *
 * @author ykalay
 *
 * @since 1.0
 */
public class NettyChannelStore {

    /**
     * Singleton lazy instance of {@link NettyChannelStore}
     */
    private static NettyChannelStore LAZY_HOLDER;

    /**
     * @return singleton lazy instance of {@link NettyChannelStore}
     */
    public static NettyChannelStore getInstance() {
        if(Objects.isNull(LAZY_HOLDER)) {
            LAZY_HOLDER = new NettyChannelStore();
        }
        return LAZY_HOLDER;
    }

    /**
     * Netty channel map
     */
    private final Map<String, TunnelChannel> nettyChannelStoreMap;
    private NettyChannelTimeoutScheduler nettyChannelTimeoutScheduler;


    private NettyChannelStore() {
        this.nettyChannelStoreMap = new ConcurrentHashMap<>();
    }

    public void setNettyChannelTimeoutScheduler(HttpAmqpTunnelTimeoutHandler httpAmqpTunnelTimeoutHandler) {
        this.nettyChannelTimeoutScheduler = NettyChannelTimeoutScheduler.getInstance(httpAmqpTunnelTimeoutHandler);
    }

    public void registerNettyChannel(AmqpMessage<?> amqpMessage, io.netty.channel.Channel channel) {
        // Schedule a timeout scheduler
        ScheduledFuture<?> timeoutTask = nettyChannelTimeoutScheduler.scheduleTimeoutTask(amqpMessage, channel);

        // Finally, we are adding channel into the nettyChannelStoreMap
        nettyChannelStoreMap.put(amqpMessage.getUniqueMessageId(), new TunnelChannel(timeoutTask, amqpMessage, channel));
    }

    public void deRegisterNettyChannel(String uniqueMessageId) {
        TunnelChannel tunnelChannel = nettyChannelStoreMap.remove(uniqueMessageId);
        if(tunnelChannel != null) {
            tunnelChannel.getTimeoutHandler().cancel(true);
        }
    }

    public AmqpMessage getNettyChannelById(String uniqueMessageId) {
        TunnelChannel tunnelChannel = nettyChannelStoreMap.get(uniqueMessageId);
        if(tunnelChannel != null) {
            return tunnelChannel.getAmqpMessage();
        }
        // We couldn't find the channel in store
        return null;
    }

    public TunnelChannel getTunnelChannel(String uniqueMessageId) {
        TunnelChannel tunnelChannel = nettyChannelStoreMap.get(uniqueMessageId);
        return tunnelChannel;
    }
}
