package com.ykalay.rabbitmqtunnel.http;

import io.netty.handler.codec.http.HttpResponseStatus;

public class TunnelHttpResponse {
    private HttpResponseStatus httpResponseStatus;
    private byte[] body;

    public TunnelHttpResponse() {}

    public TunnelHttpResponse(HttpResponseStatus httpResponseStatus, byte[] body) {
        this.httpResponseStatus = httpResponseStatus;
        this.body = body;
    }

    public HttpResponseStatus getHttpResponseStatus() {
        return httpResponseStatus;
    }

    public void setHttpResponseStatus(HttpResponseStatus httpResponseStatus) {
        this.httpResponseStatus = httpResponseStatus;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
