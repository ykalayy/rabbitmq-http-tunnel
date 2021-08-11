package com.ykalay.rabbitmqtunnel.constant;

/**
 * Handler-Enum const
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
