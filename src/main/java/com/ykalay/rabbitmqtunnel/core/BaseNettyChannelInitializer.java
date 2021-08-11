package com.ykalay.rabbitmqtunnel.core;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.Objects;

import static com.ykalay.rabbitmqtunnel.constant.NettyConst.*;

public class BaseNettyChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * Singleton lazy instance of {@link BaseNettyChannelInitializer}
     */
    private static BaseNettyChannelInitializer LAZY_HOLDER;

    /**
     * @return singleton lazy instance of {@link BaseNettyChannelInitializer}
     */
    public static BaseNettyChannelInitializer getInstance() {
        if(Objects.isNull(LAZY_HOLDER)) {
            LAZY_HOLDER = new BaseNettyChannelInitializer();
        }
        return LAZY_HOLDER;
    }

    private BaseNettyChannelInitializer() {}


    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(HTTP_CODEC.value(), new HttpServerCodec());
        pipeline.addLast(HTTP_AGGREGATOR.value() ,new HttpObjectAggregator(65536));
        pipeline.addLast(MAPPING_HANDLER.value(), new MappingHandler());
    }
}
