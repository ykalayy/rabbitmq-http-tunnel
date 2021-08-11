package com.ykalay.rabbitmqtunnel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Rabbitmq-rest-tunnel Controller annotation
 *
 * @author ykalay
 *
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RestAmqpTunnelController {
}
