package com.ykalay.rabbitmqtunnel.http;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpHeaders;

import java.nio.charset.StandardCharsets;

/**
 * Represents the Incoming http requests
 *
 * @author ykalay
 *
 * @since 1.0
 */
// TODO: THIS CLASS SHOULD Be MORE RICH
public class TunnelHttpRequest {

    private final FullHttpMessage fullHttpMessage;

    public TunnelHttpRequest(FullHttpMessage fullHttpMessage) {
        this.fullHttpMessage = fullHttpMessage;
    }

    public String getBody() {
        if(fullHttpMessage.content().isReadable()) {
            return fullHttpMessage.content().toString(StandardCharsets.UTF_8);
        }
        return "";
    }

    public HttpHeaders getHeaders() {
        return fullHttpMessage.headers();
    }
}
