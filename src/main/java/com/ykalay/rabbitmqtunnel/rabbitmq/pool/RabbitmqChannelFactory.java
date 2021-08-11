package com.ykalay.rabbitmqtunnel.rabbitmq.pool;

import com.rabbitmq.client.Channel;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;

/**
 * Rabbitmq's Channel Factory class
 *
 * @author ykalay
 *
 * @since 1.0
 */
public class RabbitmqChannelFactory implements PooledObjectFactory<Channel> {

    @Override
    public void activateObject(PooledObject<Channel> pooledObject) throws Exception {

    }

    @Override
    public void destroyObject(PooledObject<Channel> pooledObject) throws Exception {

    }

    @Override
    public PooledObject<Channel> makeObject() throws Exception {
        return null;
    }

    @Override
    public void passivateObject(PooledObject<Channel> pooledObject) throws Exception {

    }

    @Override
    public boolean validateObject(PooledObject<Channel> pooledObject) {
        return false;
    }
}
