package io.github.ykalay.rabbitmqtunnel.core;

import io.github.ykalay.rabbitmqtunnel.annotation.AmqpTunnelRequestMapper;
import io.github.ykalay.rabbitmqtunnel.config.NettyServerPreferences;
import io.github.ykalay.rabbitmqtunnel.config.RabbitmqServerConfig;
import io.github.ykalay.rabbitmqtunnel.core.netty.BaseNettyChannelInitializer;
import io.github.ykalay.rabbitmqtunnel.core.netty.NettyChannelStore;
import io.github.ykalay.rabbitmqtunnel.handler.HttpAmqpTunnelTimeoutHandler;
import io.github.ykalay.rabbitmqtunnel.handler.TunnelResponseInterceptor;
import io.github.ykalay.rabbitmqtunnel.rabbitmq.RabbitmqEnvironmentInitializer;
import io.github.ykalay.rabbitmqtunnel.rabbitmq.listener.RabbitmqQueueConsumer;
import io.github.ykalay.rabbitmqtunnel.rabbitmq.pool.RabbitmqChannelFactory;
import io.github.ykalay.rabbitmqtunnel.rabbitmq.pool.RabbitmqChannelStore;
import com.rabbitmq.client.Consumer;
import io.github.ykalay.rabbitmqtunnel.handler.HttpAmqpTunnelController;
import io.github.ykalay.rabbitmqtunnel.handler.TunnelExceptionAdviser;
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private final HttpAmqpTunnelController[] httpAmqpTunnelControllers;

    /**
     * Timeout Handler instance of tunnel server
     */
    private final HttpAmqpTunnelTimeoutHandler httpAmqpTunnelTimeoutHandler;

    /**
     * Rabbitmq HTTP->AMQP tunnel Exception adviser instance
     */
    private final TunnelExceptionAdviser tunnelExceptionAdviser;

    /**
     * Rabbitmq HTTP to AMQP tunnel Response interceptor implementation instance
     */
    private final TunnelResponseInterceptor responseInterceptor;

    /**
     * Rabbitmq channel store, We will use the channels for initialize the rabbitmq queue listeners
     */
    private final RabbitmqChannelStore rabbitmqChannelStore;

    /**
     * Netty server preferences for fetching event-loop thread, port, native-support...
     */
    private final NettyServerPreferences nettyServerPreferences;

    private final HttpAmqpControllerModelStore httpAmqpControllerModelStore;

    private NettyChannelStore nettyChannelStore;

    public RabbitmqHttpTunnelServer(HttpAmqpTunnelController[] httpAmqpTunnelControllers,
                                    HttpAmqpTunnelTimeoutHandler httpAmqpTunnelTimeoutHandler,
                                    TunnelExceptionAdviser tunnelExceptionAdviser,
                                    TunnelResponseInterceptor tunnelResponseInterceptor) {
        this.httpAmqpTunnelControllers = httpAmqpTunnelControllers;
        this.httpAmqpTunnelTimeoutHandler = httpAmqpTunnelTimeoutHandler;
        this.tunnelExceptionAdviser = tunnelExceptionAdviser;
        this.rabbitmqChannelStore = RabbitmqChannelStore.getInstance();
        this.nettyServerPreferences = NettyServerPreferences.getInstance();
        this.httpAmqpControllerModelStore = HttpAmqpControllerModelStore.getInstance();
        if(Objects.isNull(tunnelResponseInterceptor)) {
            this.responseInterceptor = TunnelResponseInterceptor.DEFAULT;
        } else {
            this.responseInterceptor = tunnelResponseInterceptor;
        }
    }

    /**
     * Starts the Rabbitmq Tunnel Server
     *
     * @throws Exception Configuration or runtime exceptions
     */
    public void start() throws Exception {
        // Initialize the Rabbitmq set-up
        RabbitmqChannelFactory.getInstance();
        RabbitmqChannelStore.getInstance();
        // Declare exchange & queue
        RabbitmqEnvironmentInitializer rabbitmqEnvironmentInitializer = RabbitmqEnvironmentInitializer.getInstance();
        // Declare the exchange with SERVICE_NAME
        rabbitmqEnvironmentInitializer.initExchange();
        // Declare the queue and bind the queue to exchange
        rabbitmqEnvironmentInitializer.initResponseQueue();

        // Init a NettyChannelStore
        this.nettyChannelStore = NettyChannelStore.getInstance();
        this.nettyChannelStore.setNettyChannelTimeoutScheduler(httpAmqpTunnelTimeoutHandler);

        // Controller Scan
        List<HttpAmqpControllerModel> httpAmqpControllerModelList = scanControllerMethods(httpAmqpTunnelControllers);
        // Set it to the controller model store in order to use it from other sides easily
        this.httpAmqpControllerModelStore.setHttpAmqpControllerModelList(httpAmqpControllerModelList);
        // Set timeoutHandler
        this.httpAmqpControllerModelStore.setTimeoutController(this.tunnelExceptionAdviser);

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
            Consumer consumer = new RabbitmqQueueConsumer(this.nettyChannelStore, this.responseInterceptor);
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
                    httpAmqpControllerModelList.add(new HttpAmqpControllerModel(method, amqpTunnelRequestMapper,httpAmqpTunnelController));
                }
            }
        }
        return httpAmqpControllerModelList;
    }
}
