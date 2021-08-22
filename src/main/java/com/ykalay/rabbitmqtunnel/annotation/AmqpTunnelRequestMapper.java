package com.ykalay.rabbitmqtunnel.annotation;

import com.ykalay.rabbitmqtunnel.constant.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Rabbitmq-http-tunnel controller request mapper
 *
 * @author ykalay
 *
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AmqpTunnelRequestMapper {

    /**
     * HttpMethod of controller function
     *
     * @since 1.0
     */
    HttpMethod httpMethod();

    /**
     *
     * @since 1.0
     */
    String path();

    /**
     * Target rabbitmq exchange name
     *
     *          It is optional, If it is undefined the server looks for
     *          {@link com.ykalay.rabbitmqtunnel.rabbitmq.model.AmqpMessage}'s targetExchange parameter
     *          and sends the message to this exchange
     *
     *          !!!NOTE: If the exchange is dynamic, please set the AMQP message targetExchange parameter
     *
     * @since 1.0
     */
    String targetExchange() default "undefined";
}
