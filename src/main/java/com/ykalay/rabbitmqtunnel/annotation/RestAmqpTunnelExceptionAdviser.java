package com.ykalay.rabbitmqtunnel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Rabbitmq REST->AMQP tunnel Exception adviser
 * Catch the possible exception and allows to the service handle them.
 * The classes that's annotated with this {@link RestAmqpTunnelExceptionAdviser} annotation.
 * Should extend the ->
 * @see TunnelExceptionAdviser
 *
 * @author ykalay
 *
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RestAmqpTunnelExceptionAdviser {
}
