package com.github.ykalay.rabbitmqtunnel.http;

import io.netty.handler.codec.http.FullHttpRequest;
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

    private final FullHttpRequest fullHttpRequest;

    public TunnelHttpRequest(FullHttpRequest fullHttpRequest) {
        this.fullHttpRequest = fullHttpRequest;
    }

    /**
     * @return Body of request as String
     */
    public String getBody() {
        if(fullHttpRequest.content().isReadable()) {
            return fullHttpRequest.content().toString(StandardCharsets.UTF_8);
        }
        return "";
    }

    /**
     * @return Header of Incoming Http Request
     */
    public HttpHeaders getHeaders() {
        return fullHttpRequest.headers();
    }

    /**
     * @return Incoming request URI
     */
    public String getUri() {
        return fullHttpRequest.getUri();
    }
}
