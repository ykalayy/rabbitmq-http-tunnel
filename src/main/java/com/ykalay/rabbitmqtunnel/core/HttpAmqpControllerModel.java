package com.ykalay.rabbitmqtunnel.core;

import com.ykalay.rabbitmqtunnel.annotation.AmqpTunnelRequestMapper;

import java.lang.reflect.Method;

public class HttpAmqpControllerModel {

    private Method controllerMethod;

    private AmqpTunnelRequestMapper amqpTunnelRequestMapper;

    public HttpAmqpControllerModel(Method controllerMethod, AmqpTunnelRequestMapper amqpTunnelRequestMapper) {
        this.controllerMethod = controllerMethod;
        this.amqpTunnelRequestMapper = amqpTunnelRequestMapper;
    }

    public Method getControllerMethod() {
        return controllerMethod;
    }

    public void setControllerMethod(Method controllerMethod) {
        this.controllerMethod = controllerMethod;
    }

    public AmqpTunnelRequestMapper getAmqpTunnelRequestMapper() {
        return amqpTunnelRequestMapper;
    }

    public void setAmqpTunnelRequestMapper(AmqpTunnelRequestMapper amqpTunnelRequestMapper) {
        this.amqpTunnelRequestMapper = amqpTunnelRequestMapper;
    }
}
