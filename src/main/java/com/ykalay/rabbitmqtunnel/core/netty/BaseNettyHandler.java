package com.ykalay.rabbitmqtunnel.core.netty;

import com.ykalay.rabbitmqtunnel.http.TunnelHttpResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;

/**
 * Base netty SimpleChannelInboundHandler
 *
 * @param <V>
 *
 * @author ykalay
 *
 * @since 1.0
 */
public abstract class BaseNettyHandler<V> extends SimpleChannelInboundHandler<V> {

    private static final Logger log = LoggerFactory.getLogger(BaseNettyHandler.class.getName());

    private static final boolean DEFAULT_AUTO_RELEASE = true;

    private static final AttributeKey<String> URL_ATTR = AttributeKey.valueOf("uri_attr");

    protected BaseNettyHandler() {
        this(DEFAULT_AUTO_RELEASE);
    }

    protected BaseNettyHandler(boolean autoRelease) {
        super(autoRelease);
    }

    public static void sendResponseWithNoBody(Channel channel, HttpResponseStatus httpResponseStatus) {
        final HttpResponse responseBody = new DefaultHttpResponse(
                HttpVersion.HTTP_1_1,
                httpResponseStatus);

        channel.writeAndFlush(responseBody)
                .addListener(ChannelFutureListener.CLOSE);
    }

    public static void sendJsonResponseWithBody(Channel channel, TunnelHttpResponse tunnelHttpResponse) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                tunnelHttpResponse.getHttpResponseStatus(), Unpooled.wrappedBuffer(tunnelHttpResponse.getBody()));
        response.headers().set(CONTENT_TYPE, "application/json");
        response.headers().set(CONTENT_LENGTH, response .content().readableBytes());

        channel.writeAndFlush(response)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("Exception caught Ex: {0}", cause);
    }

    public static void setURIAsAttr(String uri, Channel channel) {
        channel.attr(URL_ATTR).set(uri);
    }

    public static String getURIFromAttr(Channel channel) {
        return channel.attr(URL_ATTR).get();
    }
}
