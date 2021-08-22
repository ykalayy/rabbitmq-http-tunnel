package com.ykalay.rabbitmqtunnel.core.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class MappingHandler extends BaseNettyHandler<HttpRequest> {

    private static final Logger log = LoggerFactory.getLogger(MappingHandler.class.getName());

    public MappingHandler() {
        // Set the autoRelease as false, We will manage the ReferenceCount release as manually
        super(false);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) throws Exception {

        // TODO HANDLER SEARCH AND DO LOGICS
    }
}
