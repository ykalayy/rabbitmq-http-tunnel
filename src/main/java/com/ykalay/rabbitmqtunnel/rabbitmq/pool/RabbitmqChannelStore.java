package com.ykalay.rabbitmqtunnel.rabbitmq.pool;

import com.rabbitmq.client.Channel;
import com.ykalay.rabbitmqtunnel.config.RabbitmqPoolConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.Objects;

/**
 * Rabbitmq Channels Store
 *
 * @author ykalay
 *
 * @since 1.0
 */
public class RabbitmqChannelStore {

    /**
     * Singleton lazy instance of {@link RabbitmqChannelStore}
     */
    private static RabbitmqChannelStore LAZY_HOLDER;

    /**
     * @return singleton lazy instance of {@link RabbitmqChannelStore}
     */
    public static RabbitmqChannelStore getInstance() {
        if(Objects.isNull(LAZY_HOLDER)) {
            LAZY_HOLDER = new RabbitmqChannelStore();
        }
        return LAZY_HOLDER;
    }

    private GenericObjectPool<Channel> pool;

    public RabbitmqChannelStore() {
        RabbitmqChannelFactory rabbitmqChannelFactory = RabbitmqChannelFactory.getInstance();
        GenericObjectPoolConfig<Channel> channelGenericObjectPoolConfig = RabbitmqPoolConfig.getInstance().getPoolConfig();
        this.pool = new GenericObjectPool<>(rabbitmqChannelFactory, channelGenericObjectPoolConfig);
    }

    public Channel getChannel() throws Exception {

        Channel channel = this.pool.borrowObject();
        if (channel.isOpen()) {
            return channel;
        } else {
            this.returnChannel(channel);
            return this.getChannel();
        }
    }

    public void returnChannel(Channel channel) throws Exception {
        if (channel.isOpen()) {
            this.pool.returnObject(channel);
        } else {
            this.pool.invalidateObject(channel);
        }
    }
}
