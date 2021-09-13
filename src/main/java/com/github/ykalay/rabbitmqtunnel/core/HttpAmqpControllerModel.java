package com.github.ykalay.rabbitmqtunnel.core;

import com.github.ykalay.rabbitmqtunnel.annotation.AmqpTunnelRequestMapper;
import com.github.ykalay.rabbitmqtunnel.handler.HttpAmqpTunnelController;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class HttpAmqpControllerModel {

    private final HttpAmqpTunnelController controllerInstance;

    private Method controllerMethod;

    private AmqpTunnelRequestMapper amqpTunnelRequestMapper;

    private Pattern pattern;

    public HttpAmqpControllerModel(Method controllerMethod, AmqpTunnelRequestMapper amqpTunnelRequestMapper,
                                   HttpAmqpTunnelController controllerInstance) {
        this.controllerInstance = controllerInstance;
        this.controllerMethod = controllerMethod;
        this.amqpTunnelRequestMapper = amqpTunnelRequestMapper;
        this.pattern = Pattern.compile(amqpTunnelRequestMapper.path());
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
        this.pattern = Pattern.compile(amqpTunnelRequestMapper.path());
    }

    public Pattern getPattern() {
        return pattern;
    }

    public HttpAmqpTunnelController getControllerInstance() {
        return controllerInstance;
    }
}
