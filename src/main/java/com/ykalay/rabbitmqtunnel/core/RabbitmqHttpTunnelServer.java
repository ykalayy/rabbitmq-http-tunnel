package com.ykalay.rabbitmqtunnel.core;

import com.rabbitmq.client.Consumer;
import com.ykalay.rabbitmqtunnel.annotation.AmqpTunnelRequestMapper;
import com.ykalay.rabbitmqtunnel.config.NettyServerPreferences;
import com.ykalay.rabbitmqtunnel.config.RabbitmqServerConfig;
import com.ykalay.rabbitmqtunnel.core.netty.BaseNettyChannelInitializer;
import com.ykalay.rabbitmqtunnel.core.netty.NettyChannelStore;
import com.ykalay.rabbitmqtunnel.handler.HttpAmqpTunnelController;
import com.ykalay.rabbitmqtunnel.handler.HttpAmqpTunnelTimeoutHandler;
import com.ykalay.rabbitmqtunnel.handler.TunnelExceptionAdviser;
import com.ykalay.rabbitmqtunnel.rabbitmq.RabbitmqEnvironmentInitializer;
import com.ykalay.rabbitmqtunnel.rabbitmq.listener.RabbitmqQueueConsumer;
import com.ykalay.rabbitmqtunnel.rabbitmq.pool.RabbitmqChannelFactory;
import com.ykalay.rabbitmqtunnel.rabbitmq.pool.RabbitmqChannelStore;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *  Rabbitmq Tunnel Server
 *
 *      Initialize the netty, queues, exchanges, scans the HttpMapper annotations and register them
 *
 * @author ykalay
 *
 * @since 1.0
 */
public class RabbitmqHttpTunnelServer {

    private static final Logger log = LoggerFactory.getLogger(RabbitmqHttpTunnelServer.class.getName());

    /**
     * Rabbitmq-http-tunnel Controller instances
     */
    private HttpAmqpTunnelController[] httpAmqpTunnelControllers;

    /**
     * Timeout Handler instance of tunnel server
     */
    private HttpAmqpTunnelTimeoutHandler httpAmqpTunnelTimeoutHandler;

    /**
     * Rabbitmq HTTP->AMQP tunnel Exception adviser instance
     */
    private TunnelExceptionAdviser tunnelExceptionAdviser;

    /**
     * Initializer of Rabbitmq Environment class instance
     */
    private RabbitmqEnvironmentInitializer rabbitmqEnvironmentInitializer;

    /**
     * Rabbitmq channel store, We will use the channels for initialize the rabbitmq queue listeners
     */
    private RabbitmqChannelStore rabbitmqChannelStore;

    /**
     * Netty server preferences for fetching event-loop thread, port, native-support...
     */
    private NettyServerPreferences nettyServerPreferences;

    private HttpAmqpControllerModelStore httpAmqpControllerModelStore;

    public RabbitmqHttpTunnelServer(HttpAmqpTunnelController[] httpAmqpTunnelControllers,
                                    HttpAmqpTunnelTimeoutHandler httpAmqpTunnelTimeoutHandler,
                                    TunnelExceptionAdviser tunnelExceptionAdviser) {
        this.httpAmqpTunnelControllers = httpAmqpTunnelControllers;
        this.httpAmqpTunnelTimeoutHandler = httpAmqpTunnelTimeoutHandler;
        this.tunnelExceptionAdviser = tunnelExceptionAdviser;
        this.rabbitmqChannelStore = RabbitmqChannelStore.getInstance();
        this.httpAmqpControllerModelStore = HttpAmqpControllerModelStore.getInstance();
    }

    /**
     * Starts the Rabbitmq Tunnel Server
     */
    public void start() throws Exception {
        // Initialize the Rabbitmq set-up
        RabbitmqChannelFactory.getInstance();
        RabbitmqChannelStore.getInstance();
        // Declare exchange & queue
        this.rabbitmqEnvironmentInitializer = RabbitmqEnvironmentInitializer.getInstance();
        // Declare the exchange with SERVICE_NAME
        this.rabbitmqEnvironmentInitializer.initExchange();
        // Declare the queue and bind the queue to exchange
        this.rabbitmqEnvironmentInitializer.initResponseQueue();

        // Init a NettyChannelStore
        NettyChannelStore.getInstance(httpAmqpTunnelTimeoutHandler);

        // Controller Scan
        List<HttpAmqpControllerModel> httpAmqpControllerModelList = scanControllerMethods(httpAmqpTunnelControllers);
        // Set it to the controller model store in order to use it from other sides easily
        this.httpAmqpControllerModelStore.setHttpAmqpControllerModelList(httpAmqpControllerModelList);

        // Rabbitmq Consumer
        initRabbitmqConsumers();

        // Server Start
        startNettyServer();

        // Netty handlers & exception
    }

    private void startNettyServer() {
        boolean nativeSupport = this.nettyServerPreferences.isNativeSupport();
        int eventLoopSize = this.nettyServerPreferences.getNettyEventLoopSize();
        int port = this.nettyServerPreferences.getNettyPort();
        EventLoopGroup bossGroup =  nativeSupport ?
                new EpollEventLoopGroup(1)
                : new NioEventLoopGroup(1);

        EventLoopGroup workerGroup =  nativeSupport ?
                new EpollEventLoopGroup(eventLoopSize)
                : new NioEventLoopGroup(eventLoopSize);

        Class<? extends ServerChannel> channelClass = nativeSupport ?
                EpollServerSocketChannel.class :
                NioServerSocketChannel.class;
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(channelClass)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childHandler(BaseNettyChannelInitializer.getInstance());

            bootstrap
                    .bind(port)
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();
        } catch (InterruptedException interruptedException) {
            log.error("Interrupted Exception is occurred. Exception : {0}", interruptedException);
            Thread.currentThread().interrupt();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private void initRabbitmqConsumers() throws Exception {

        for(int i = 0; i < RabbitmqServerConfig.getInstance().getSelfQueueConsumerCount(); i++) {
            com.rabbitmq.client.Channel channel = this.rabbitmqChannelStore.getChannel();
            Consumer consumer = new RabbitmqQueueConsumer();
            channel.basicConsume(RabbitmqServerConfig.getInstance().getQueueName(), true, consumer);
        }
    }

    private List<HttpAmqpControllerModel> scanControllerMethods(HttpAmqpTunnelController[] httpAmqpTunnelControllers) {
        List<HttpAmqpControllerModel> httpAmqpControllerModelList = new ArrayList<>();
        for (HttpAmqpTunnelController httpAmqpTunnelController : httpAmqpTunnelControllers) {
           Class<?> controllerClass = httpAmqpTunnelController.getClass();
            for (Method method : controllerClass.getMethods()) {
                // Check the method is annotated with AmqpTunnelRequestMapper
                if(method.isAnnotationPresent(AmqpTunnelRequestMapper.class)) {
                    AmqpTunnelRequestMapper amqpTunnelRequestMapper = method.getAnnotation(AmqpTunnelRequestMapper.class);
                    httpAmqpControllerModelList.add(new HttpAmqpControllerModel(method, amqpTunnelRequestMapper));
                }
            }
        }
        return httpAmqpControllerModelList;
    }
}
