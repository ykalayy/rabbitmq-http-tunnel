package com.ykalay.rabbitmqtunnel.core.netty;

import com.ykalay.rabbitmqtunnel.core.netty.model.TunnelChannel;
import com.ykalay.rabbitmqtunnel.handler.HttpAmqpTunnelTimeoutHandler;
import com.ykalay.rabbitmqtunnel.rabbitmq.model.AmqpMessage;

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
    public static NettyChannelStore getInstance(HttpAmqpTunnelTimeoutHandler httpAmqpTunnelTimeoutHandler) {
        if(Objects.isNull(LAZY_HOLDER)) {
            LAZY_HOLDER = new NettyChannelStore(httpAmqpTunnelTimeoutHandler);
        }
        return LAZY_HOLDER;
    }

    /**
     * Netty channel map
     */
    private final Map<String, TunnelChannel> nettyChannelStoreMap;
    private final NettyChannelTimeoutScheduler nettyChannelTimeoutScheduler;


    private NettyChannelStore(HttpAmqpTunnelTimeoutHandler httpAmqpTunnelTimeoutHandler) {
        this.nettyChannelStoreMap = new ConcurrentHashMap<>();
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
