package com.ykalay.rabbitmqtunnel.core.netty;

import com.ykalay.rabbitmqtunnel.config.NettyServerPreferences;
import com.ykalay.rabbitmqtunnel.handler.HttpAmqpTunnelTimeoutHandler;
import com.ykalay.rabbitmqtunnel.rabbitmq.model.AmqpMessage;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.ykalay.rabbitmqtunnel.config.NettyServerPreferences.DEFAULT_TIMEOUT_SCHEDULER_THREAD_SIZE;

/**
 * Netty Timeout Scheduler of rabbitmq-http tunnel server
 *
 * It schedules a timeout runnable for incoming requests
 *
 * @author ykalay
 *
 * @since 1.0
 */
public class NettyChannelTimeoutScheduler {

    /**
     * Singleton lazy instance of {@link NettyChannelTimeoutScheduler}
     */
    private static NettyChannelTimeoutScheduler LAZY_HOLDER;

    /**
     * @return singleton lazy instance of {@link NettyChannelTimeoutScheduler}
     */
    public static NettyChannelTimeoutScheduler getInstance(HttpAmqpTunnelTimeoutHandler httpAmqpTunnelTimeoutHandler) {
        if(Objects.isNull(LAZY_HOLDER)) {
            LAZY_HOLDER = new NettyChannelTimeoutScheduler(httpAmqpTunnelTimeoutHandler);
        }
        return LAZY_HOLDER;
    }

    private final ScheduledThreadPoolExecutor executor;
    private final HttpAmqpTunnelTimeoutHandler httpAmqpTunnelTimeoutHandler;
    private final int timeoutSec;

    private NettyChannelTimeoutScheduler(HttpAmqpTunnelTimeoutHandler httpAmqpTunnelTimeoutHandler) {
        this.httpAmqpTunnelTimeoutHandler = httpAmqpTunnelTimeoutHandler;
        this.timeoutSec = NettyServerPreferences.getInstance().getNettyTimeoutSec();
        this.executor = (ScheduledThreadPoolExecutor) Executors
                .newScheduledThreadPool(DEFAULT_TIMEOUT_SCHEDULER_THREAD_SIZE);
    }

    public ScheduledFuture<?> scheduleTimeoutTask(AmqpMessage<?> amqpMessage, io.netty.channel.Channel channel) {
        return executor.schedule(new NettyChannelTimeoutTask(channel, httpAmqpTunnelTimeoutHandler, amqpMessage),
                timeoutSec, TimeUnit.SECONDS);
    }
}
