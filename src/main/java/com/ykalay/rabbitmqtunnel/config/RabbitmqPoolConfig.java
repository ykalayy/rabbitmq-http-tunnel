package com.ykalay.rabbitmqtunnel.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.Objects;

/**
 * Rabbitmq Pool configuration class
 *
 * @author ykalay
 *
 * @since 1.0
 */
public class RabbitmqPoolConfig {


    /**
     * Singleton lazy instance of {@link RabbitmqPoolConfig}
     */
    private static RabbitmqPoolConfig LAZY_HOLDER;

    /**
     * @return singleton lazy instance of {@link RabbitmqPoolConfig}
     */
    public static RabbitmqPoolConfig getInstance() {
        if(Objects.isNull(LAZY_HOLDER)) {
            LAZY_HOLDER = new RabbitmqPoolConfig();
        }
        return LAZY_HOLDER;
    }

    private RabbitmqPoolConfig() {init();}

    private GenericObjectPoolConfig<com.rabbitmq.client.Channel> DEFAULT_POOL_CONFIG = new GenericObjectPoolConfig<>();

    private GenericObjectPoolConfig<com.rabbitmq.client.Channel> poolConfig;

    private void init() {
        int numProcessor = Runtime.getRuntime().availableProcessors();
        // Default config channel pool
        DEFAULT_POOL_CONFIG.setMaxTotal(numProcessor * numProcessor);
        DEFAULT_POOL_CONFIG.setMaxIdle(numProcessor * numProcessor);
        DEFAULT_POOL_CONFIG.setMinIdle(numProcessor);
        DEFAULT_POOL_CONFIG.setNumTestsPerEvictionRun(numProcessor * 2);
        DEFAULT_POOL_CONFIG.setBlockWhenExhausted(false);
        DEFAULT_POOL_CONFIG.setMinEvictableIdleTimeMillis(3600000);
        DEFAULT_POOL_CONFIG.setTimeBetweenEvictionRunsMillis(1500000);

        // Set is as default at the first time
        if(Objects.isNull(poolConfig)) {
            this.poolConfig = DEFAULT_POOL_CONFIG;
        }
    }

    public GenericObjectPoolConfig<com.rabbitmq.client.Channel> getPoolConfig() {
        // If the custom poolConfig is not given just use the DEFAULT one
        if(Objects.isNull(poolConfig)) {
            return DEFAULT_POOL_CONFIG;
        }
        return poolConfig;
    }

    public void setPoolConfig(GenericObjectPoolConfig<com.rabbitmq.client.Channel> poolConfig) {
        this.poolConfig = poolConfig;
    }
}
