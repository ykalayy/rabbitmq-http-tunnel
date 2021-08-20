package com.ykalay.rabbitmqtunnel.core;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base netty SimpleChannelInboundHandler
 * @param <V>
 * @author ykalay
 */
public abstract class BaseNettyHandler<V> extends SimpleChannelInboundHandler<V> {

    private static final Logger log = LoggerFactory.getLogger(BaseNettyHandler.class.getName());

    private static final boolean DEFAULT_AUTO_RELEASE = true;

    protected BaseNettyHandler() {
        this(DEFAULT_AUTO_RELEASE);
    }

    protected BaseNettyHandler(boolean autoRelease) {
        super(autoRelease);
    }

    protected void sendResponseWithNoBody(ChannelHandlerContext ctx, HttpResponseStatus httpResponseStatus) {
        final HttpResponse badRequestResponse = new DefaultHttpResponse(
                HttpVersion.HTTP_1_1,
                httpResponseStatus);

        ctx.writeAndFlush(badRequestResponse)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("Exception caught Ex: {0}", cause);
    }
}
