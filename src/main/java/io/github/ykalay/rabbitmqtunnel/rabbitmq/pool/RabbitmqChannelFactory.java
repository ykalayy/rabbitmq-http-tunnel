package io.github.ykalay.rabbitmqtunnel.rabbitmq.pool;

import io.github.ykalay.rabbitmqtunnel.rabbitmq.connection.RabbitmqSingleConnectionPool;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.Objects;

/**
 * Rabbitmq's Channel Factory class
 *
 * @author ykalay
 *
 * @since 1.0
 */
public class RabbitmqChannelFactory implements PooledObjectFactory<Channel> {

    private static RabbitmqChannelFactory rabbitmqChannelFactory;

    public static RabbitmqChannelFactory getInstance() {
        if(Objects.isNull(rabbitmqChannelFactory)) {
            rabbitmqChannelFactory = new RabbitmqChannelFactory();
        }
        return rabbitmqChannelFactory;
    }

    private RabbitmqSingleConnectionPool rabbitmqSingleConnection;
    private Connection connection;

    private RabbitmqChannelFactory() {
        this.rabbitmqSingleConnection = RabbitmqSingleConnectionPool.getInstance();
        this.connection = this.rabbitmqSingleConnection.getRabbitMqConnection();
    }

    @Override
    public PooledObject<Channel> makeObject() throws Exception {
        return new DefaultPooledObject<>(connection.createChannel());
    }

    @Override
    public void destroyObject(PooledObject<Channel> pooledObject) throws Exception {
        Channel channel = pooledObject.getObject();
        if(channel.isOpen()) {
            channel.close();
        }
    }

    @Override
    public boolean validateObject(PooledObject<Channel> pooledObject) {
        final Channel channel = pooledObject.getObject();
        return channel.isOpen();
    }

    @Override
    public void passivateObject(PooledObject<Channel> pooledObject) throws Exception {

    }

    @Override
    public void activateObject(PooledObject<Channel> pooledObject) throws Exception {

    }
}
