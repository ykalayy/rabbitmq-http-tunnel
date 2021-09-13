package com.github.ykalay.rabbitmqtunnel.config;

import com.github.ykalay.rabbitmqtunnel.core.netty.NettyChannelTimeoutScheduler;
import com.github.ykalay.rabbitmqtunnel.support.SystemEnvironmentUtil;

import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Netty related config values preference class
 *
 * @implSpec  System Environments:
 *          NETTY_PORT: Port of netty to listen incoming HTTP request
 *          NETTY_EVENT_LOOP_SIZE: Size of EventLoopThread
 *          NETTY_NATIVE_SUPPORT: Native support of netty
 *          HTTP_TIMEOUT_SEC: Timeout second
 *
 * @author ykalay
 *
 * @since 1.0
 *
 */
public class NettyServerPreferences {

    /**
     * Singleton lazy instance of {@link NettyServerPreferences}
     */
    private static NettyServerPreferences LAZY_HOLDER;

    /**
     * @return singleton lazy instance of {@link NettyServerPreferences}
     */
    public static NettyServerPreferences getInstance() {
        if(Objects.isNull(LAZY_HOLDER)) {
            LAZY_HOLDER = new NettyServerPreferences();
        }
        return LAZY_HOLDER;
    }

    private NettyServerPreferences() {init();}

    /**
     * Default netty port
     */
    private static final int DEFAULT_NETTY_PORT = 8080;

    /**
     * Default netty event-loop size
     * Available Processor size * 2
     */
    private static final int DEFAULT_NETTY_EVENT_LOOP_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * Default {@link ScheduledThreadPoolExecutor} core-pool size
     *
     * @see NettyChannelTimeoutScheduler
     */
    public static final int DEFAULT_TIMEOUT_SCHEDULER_THREAD_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * Default timeout of http requests
     */
    private static final int DEFAULT_TIMEOUT_SEC = 60;

    /**
     * Default native support of netty(epoll)
     * More details please check:
     *
     * @see <a href="https://netty.io/wiki/native-transports.html">https://netty.io/wiki/native-transports.html</a>
     *
     * Be careful Alpine container lovers. Because Alpine currently doesn't support the epoll
     */
    private static boolean DEFAULT_NATIVE_SUPPORT = false;

    /**
     * Netty server port
     */
    private int nettyPort;

    /**
     * Netty event-loop size
     */
    private int nettyEventLoopSize;

    /**
     * Native support of netty(epoll)
     * More details please check:
     *
     * @see <a href="https://netty.io/wiki/native-transports.html">https://netty.io/wiki/native-transports.html</a>
     *
     * Be careful Alpine container lovers. Because Alpine currently doesn't support the epoll
     */
    private boolean nativeSupport;

    /**
     * Netty timeout sec
     */
    private int nettyTimeoutSec;

    /**
     * @return Netty-port value
     */
    public int getNettyPort() {
        return nettyPort;
    }

    /**
     * Sets the netty-port config parameter
     *
     * @param nettyPort
     *          Netty-server port
     */
    public void setNettyPort(int nettyPort) {
        this.nettyPort = nettyPort;
    }

    /**
     * @return Netty-event loop size
     */
    public int getNettyEventLoopSize() {
        return nettyEventLoopSize;
    }

    /**
     * Sets the netty event loop size
     *
     * @param nettyEventLoopSize
     *          New eventLoopThread size
     */
    public void setNettyEventLoopSize(int nettyEventLoopSize) {
        this.nettyEventLoopSize = nettyEventLoopSize;
    }

    /**
     * @return true, If native support is enabled
     */
    public boolean isNativeSupport() {
        return nativeSupport;
    }

    /**
     * Set-up the native support
     *
     * @param nativeSupport
     *          Netty-epoll support value
     */
    public void setNativeSupport(boolean nativeSupport) {
        this.nativeSupport = nativeSupport;
    }

    /**
     * @return Timeout-value of nett-server
     */
    public int getNettyTimeoutSec() {
        return nettyTimeoutSec;
    }

    /**
     * Sets the netty http timeout sec
     *
     * @param nettyTimeoutSec
     *          New timeout value(sec)
     */
    public void setNettyTimeoutSec(int nettyTimeoutSec) {
        this.nettyTimeoutSec = nettyTimeoutSec;
    }

    /**
     * Initialize the default config values of netty-server
     */
    private void init() {

        // Set-up netty port
        if (SystemEnvironmentUtil.getInt("NETTY_PORT", -1) == -1) {
            this.setNettyPort(DEFAULT_NETTY_PORT);
        } else {
            this.setNettyPort(SystemEnvironmentUtil.getInt("NETTY_PORT", -1));
        }

        // Set-up netty event-loop size
        if(SystemEnvironmentUtil.getInt("NETTY_EVENT_LOOP_SIZE", -1) == -1) {
            this.setNettyEventLoopSize(DEFAULT_NETTY_EVENT_LOOP_SIZE);
        } else {
            this.setNettyEventLoopSize(SystemEnvironmentUtil.getInt("NETTY_EVENT_LOOP_SIZE", -1));
        }

        // Set-up native support of netty
        if(SystemEnvironmentUtil.getBool("NETTY_NATIVE_SUPPORT") == null) {
            this.setNativeSupport(DEFAULT_NATIVE_SUPPORT);
        } else {
            this.setNativeSupport(SystemEnvironmentUtil.getBool("NETTY_NATIVE_SUPPORT"));
        }

        if(SystemEnvironmentUtil.getInt("HTTP_TIMEOUT_SEC", -1) == -1) {
            this.setNettyTimeoutSec(DEFAULT_TIMEOUT_SEC);
        } else {
            this.setNettyTimeoutSec(SystemEnvironmentUtil.getInt("HTTP_TIMEOUT_SEC", -1));
        }
    }
}
