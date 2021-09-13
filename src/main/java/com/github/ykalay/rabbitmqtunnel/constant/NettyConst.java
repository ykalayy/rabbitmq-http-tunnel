package com.github.ykalay.rabbitmqtunnel.constant;

/**
 * Handler-Enum const
 *
 * @author ykalay
 *
 * @since 1.0
 */
public enum NettyConst {

    HTTP_CODEC("httpServerCodec"),
    HTTP_AGGREGATOR("httpAggregator"),
    MAPPING_HANDLER("mappingHandler");

    private final String value;

    NettyConst(String value) {
        this.value = value;
    }

    public String value(){ return this.value;}
}
