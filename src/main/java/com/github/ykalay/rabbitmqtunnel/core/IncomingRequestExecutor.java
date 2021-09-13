package com.github.ykalay.rabbitmqtunnel.core;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class IncomingRequestExecutor {

    /**
     * Singleton lazy instance of {@link IncomingRequestExecutor}
     */
    private static IncomingRequestExecutor LAZY_HOLDER;

    /**
     * @return singleton lazy instance of {@link IncomingRequestExecutor}
     */
    public static IncomingRequestExecutor getInstance() {
        if(Objects.isNull(LAZY_HOLDER)) {
            LAZY_HOLDER = new IncomingRequestExecutor();
        }
        return LAZY_HOLDER;
    }

    private final ExecutorService executorService;

    public IncomingRequestExecutor() {
        final ThreadFactory namedThreadFactory = new DefaultThreadFactory("asyncNettyTaskExecutor");
        this.executorService = Executors.newCachedThreadPool(namedThreadFactory);
    }

    public void submitWork(Runnable task) {
        this.executorService.submit(task);
    }
}
