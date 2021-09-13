package io.github.ykalay.rabbitmqtunnel.annotation;

import io.github.ykalay.rabbitmqtunnel.constant.HttpMethod;
import io.github.ykalay.rabbitmqtunnel.rabbitmq.model.AmqpMessage;

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
     * HttpMethod of controller function(GET/POST/PUT/PATCH...)
     *
     * @return http-method
     * @since 1.0
     */
    HttpMethod httpMethod();

    /**
     * Request path
     *
     * @return path
     * @since 1.0
     */
    String path();

    /**
     * Target rabbitmq exchange name
     *
     *          It is optional, If it is undefined the server looks for
     *          {@link AmqpMessage}'s targetExchange parameter
     *          and sends the message to this exchange
     *
     *          !!!NOTE: If the exchange is dynamic, please set the AMQP message targetExchange parameter
     *
     * @return target-exchange name
     * @since 1.0
     */
    String targetExchange() default "undefined";
}
