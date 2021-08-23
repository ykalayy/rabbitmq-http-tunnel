package com.ykalay.rabbitmqtunnel.core;

import com.ykalay.rabbitmqtunnel.annotation.AmqpTunnelRequestMapper;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class HttpAmqpControllerModel {

    private Method controllerMethod;

    private AmqpTunnelRequestMapper amqpTunnelRequestMapper;

    private Pattern pattern;

    public HttpAmqpControllerModel(Method controllerMethod, AmqpTunnelRequestMapper amqpTunnelRequestMapper) {
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
}
