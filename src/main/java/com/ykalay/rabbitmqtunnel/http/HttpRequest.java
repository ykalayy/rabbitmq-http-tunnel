package com.ykalay.rabbitmqtunnel.http;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpHeaders;

/**
 * Represents the Incoming http requests
 *
 * @author ykalay
 *
 * @since 1.0
 */
public class HttpRequest {

    private final FullHttpMessage fullHttpMessage;

    public HttpRequest(FullHttpMessage fullHttpMessage) {
        this.fullHttpMessage = fullHttpMessage;
    }

    public byte[] getBody() {
        if(fullHttpMessage.content().isReadable()) {
            return fullHttpMessage.content().array();
        }
        return new byte[0];
    }

    public HttpHeaders getHeaders() {
        return fullHttpMessage.headers();
    }
}
