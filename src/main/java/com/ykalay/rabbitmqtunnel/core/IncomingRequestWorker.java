package com.ykalay.rabbitmqtunnel.core;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class IncomingRequestWorker {

    /**
     * Singleton lazy instance of {@link IncomingRequestWorker}
     */
    private static IncomingRequestWorker LAZY_HOLDER;

    /**
     * @return singleton lazy instance of {@link IncomingRequestWorker}
     */
    public static IncomingRequestWorker getInstance() {
        if(Objects.isNull(LAZY_HOLDER)) {
            LAZY_HOLDER = new IncomingRequestWorker();
        }
        return LAZY_HOLDER;
    }

    private final ExecutorService executorService;

    public IncomingRequestWorker() {
        final ThreadFactory namedThreadFactory = new DefaultThreadFactory("asyncNettyTaskExecutor");
        this.executorService = Executors.newCachedThreadPool(namedThreadFactory);
    }

    public void submitWork(Runnable task) {
        this.executorService.submit(task);
    }
}
